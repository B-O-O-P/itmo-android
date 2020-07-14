package com.chizhikov.weatherapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.chizhikov.weatherapp.dailyList.DailyForecastListAdapter
import com.chizhikov.weatherapp.dailyList.DailyForecastView
import com.chizhikov.weatherapp.realmDB.*
import com.chizhikov.weatherapp.utils.RealmUtility
import com.chizhikov.weatherapp.utils.WeatherUtils
import com.chizhikov.weatherapp.utils.findDiffInMinutes
import com.chizhikov.weatherapp.utils.getCurrentTime
import com.chizhikov.weatherapp.weatherApi.City
import com.chizhikov.weatherapp.weatherApi.DailyForecasts
import com.chizhikov.weatherapp.weatherApi.DayForecast
import com.chizhikov.weatherapp.weatherApi.HourForecast
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.general_forecast_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val LOG_TAG = "Weather API"
private const val UPDATE_TIME_IN_MINUTES = 5

@RequiresApi(Build.VERSION_CODES.N)
class MainActivity : AppCompatActivity() {
    private val API_KEY = BuildConfig.API_KEY
    private var themeIsLight: Boolean = true
    private var currentCityKey: Int? = null
    private var currentCityName: String = ""
    private lateinit var adapter: DailyForecastListAdapter
    private var listState: Parcelable? = null
    private var realm: Realm = Realm.getInstance(RealmUtility.defaultConfig)
    private var realmModel: AccuWeatherRealmModel = AccuWeatherRealmModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeIsLight = savedInstanceState?.getBoolean(getString(R.string.RESTORE_THEME)) ?: true
        if (themeIsLight) {
            setTheme(R.style.LightTheme)
        } else {
            setTheme(R.style.DarkTheme)
        }
        setContentView(R.layout.activity_main)

        initGeneralForecast()
        initDailyForecasts()


        if (!updatedNeeded()) {
            if (!loadDataFromDB(realm)) {
                clearDB(realm)

                loadDataFromApi()

                if (savedInstanceState != null) {
                    daily_info_list.layoutManager?.onRestoreInstanceState(listState)
                }
            }
        } else {
            loadDataFromApi()
        }

        updateTheme()
        setFullscreen()

        main_refresh.setColorSchemeResources(R.color.LightBlueTextColor)

        main_refresh.setOnRefreshListener {
            main_refresh.isRefreshing = true
            if (updatedNeeded()) {
                loadDataFromApi()
            }
            main_refresh.isRefreshing = false
        }
    }


    //LAYOUT FUNCTIONS
    fun onChangeThemeClick(v: View) {
        themeIsLight = !themeIsLight
        recreate()
    }

    private fun dayOrNight(): String {
        return if (themeIsLight) {
            "Day"
        } else {
            "Night"
        }
    }

    private fun setFullscreen() {
        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        }

    }

    //TIME AND UPDATE
    private fun getTime(realm: Realm): Long {
        val time: TimeDB? = realmModel.getTime(realm)
        return time?.value ?: getCurrentTime().time
    }

    private fun updatedNeeded(): Boolean {
        return findDiffInMinutes(getCurrentTime().time, getTime(realm)) > UPDATE_TIME_IN_MINUTES
    }

    private fun updateTheme() {
        if (themeIsLight) {
            imageButton.setImageResource(R.drawable.ic_theme_day)
        } else {
            imageButton.setImageResource(R.drawable.ic_theme_night)
        }
    }


    //SAVING USEFUL THINGS
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(getString(R.string.RESTORE_THEME), themeIsLight)
        val listState = daily_info_list.layoutManager?.onSaveInstanceState()
        outState.putParcelable("LIST_STATE", listState)
        currentCityKey?.let { outState.putInt("CITY_KEY", it) }
        outState.putString("CITY_NAME", currentCityName)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        themeIsLight = savedInstanceState.getBoolean(getString(R.string.RESTORE_THEME))
        currentCityKey = savedInstanceState.getInt("CITY_KEY")
        listState = savedInstanceState.getParcelable("LIST_STATE")
        currentCityName = savedInstanceState.getString("CITY_NAME").toString()
    }


    //INITIALIZE
    private fun initGeneralForecast() {
        general_forecast_item_1.image_for_value.setImageResource(R.drawable.weather_windy)
        general_forecast_item_1.value_description.text =
            getString(R.string.WindFlow)
        general_forecast_item_2.image_for_value.setImageResource(R.drawable.weather_rainy)
        general_forecast_item_2.value_description.text =
            getString(R.string.Precipitation)
        general_forecast_item_3.image_for_value.setImageResource(R.drawable.weather_humidity)
        general_forecast_item_3.value_description.text =
            getString(R.string.Humidity)
    }

    private fun initDailyForecasts() {
        adapter = DailyForecastListAdapter(emptyList())
        daily_info_list.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@MainActivity.adapter
        }
    }


    //LOAD FROM API

    private fun loadDataFromApi() {
        clearDB(realm)
        loadForecastFromApi("RU", "Saint Petersburg")
    }

    private fun loadForecastFromApi(countryID: String, query: String) {
        val cityCall: Call<List<City>> =
            WeatherApp.app.accuWeatherApi.getCity(countryID, API_KEY, query)
        cityCall.enqueue(object : Callback<List<City>> {
            override fun onFailure(call: Call<List<City>>, t: Throwable) {
                Log.e(LOG_TAG, "Failed with", t)
            }

            override fun onResponse(call: Call<List<City>>, response: Response<List<City>>) {
                val body = response.body()
                val currentCity = body?.get(0)
                currentCityKey = currentCity?.locationKey
                if (currentCity != null) {
                    val currentCityString =
                        "${currentCity.englishName}, ${currentCity.country.englishName}"
                    current_location.text = currentCityString
                    currentCityName = currentCityString
                    saveCityToDB(realm, currentCity)
                }
                loadHourForecastFromApi(currentCityKey, true, true)
                loadDailyForecastsFromApi(currentCityKey, true)
            }
        })
    }

    private fun loadHourForecastFromApi(locationKey: Int?, details: Boolean, metric: Boolean) {
        if (locationKey != null) {
            val hourCall: Call<List<HourForecast>> =
                WeatherApp.app.accuWeatherApi.getHourForecast(locationKey, API_KEY, details, metric)
            hourCall.enqueue(object : Callback<List<HourForecast>> {
                override fun onFailure(call: Call<List<HourForecast>>, t: Throwable) {
                    Log.e(LOG_TAG, "Failed with", t)
                }

                override fun onResponse(
                    call: Call<List<HourForecast>>,
                    response: Response<List<HourForecast>>
                ) {
                    val forecast = response.body()?.get(0)
                    if (forecast != null) {
                        updateHourForecast(forecast)
                        saveHourForecastToDB(realm, forecast)
                    }
                }

            })
        }
    }

    private fun loadDailyForecastsFromApi(locationKey: Int?, metric: Boolean) {
        if (locationKey != null) {
            val dailyCall: Call<DailyForecasts> =
                WeatherApp.app.accuWeatherApi.getDailyForecasts(
                    locationKey,
                    API_KEY, metric
                )
            dailyCall.enqueue(object : Callback<DailyForecasts> {
                override fun onFailure(call: Call<DailyForecasts>, t: Throwable) {
                    Log.e(LOG_TAG, "Failed with", t)
                }

                override fun onResponse(
                    call: Call<DailyForecasts>,
                    response: Response<DailyForecasts>
                ) {
                    val dailyForecasts = response.body()?.forecasts
                    val dailyForecastListView = ArrayList<DailyForecastView>()
                    dailyForecasts?.forEach {
                        dailyForecastListView.add(convertForecastToView(it, dayOrNight()))
                    }
                    updateDailyForecast(dailyForecastListView)
                    if (dailyForecasts != null) {
                        saveDailyForecasts(realm, DailyForecasts(dailyForecasts))
                    }
                }
            })
        }
    }


    //UPDATE LAYOUT
    private fun updateGeneralForecast(
        mainForecast: String,
        temperature: String,
        wind: String,
        precipitation: String,
        humidity: String
    ) {
        current_location.text = currentCityName
        current_weather_forecast.text = WeatherUtils().convertWeatherType(mainForecast)
        main_forecast_image.setImageResource(WeatherUtils().getResource(mainForecast, dayOrNight()))

        current_temperature.text = temperature

        general_forecast_item_1.main_value.text = wind
        general_forecast_item_2.main_value.text = precipitation
        general_forecast_item_3.main_value.text = humidity
    }

    private fun updateHourForecast(
        forecast: HourForecast
    ) {
        current_weather_forecast.text = forecast.weatherForecast
        val currentTemperature =
            "${forecast.temperature.value} ${forecast.temperature.unit}"
        current_temperature.text = currentTemperature
        val currentWindValue =
            "${forecast.wind.speed.value} ${forecast.wind.speed.unit}"
        general_forecast_item_1.main_value.text = currentWindValue
        val currentPrecipitation = "${forecast.precipitation}%"
        general_forecast_item_2.main_value.text = currentPrecipitation
        val currentHumidity = forecast.humidity.toString()
        general_forecast_item_3.main_value.text = currentHumidity

        updateGeneralForecast(
            forecast.weatherForecast,
            currentTemperature,
            currentWindValue,
            currentPrecipitation,
            currentHumidity
        )
    }

    private fun updateDailyForecast(currentDailyForecasts: List<DailyForecastView>) {
        adapter.forecasts = currentDailyForecasts
        adapter.notifyDataSetChanged()
    }

    private fun convertForecastToView(forecast: DayForecast, time: String): DailyForecastView {
        val weatherForecast: String = if (time == "Day") {
            forecast.weatherForecastDay.forecast
        } else {
            forecast.weatherForecastNight.forecast
        }
        return DailyForecastView(
            WeatherUtils().getDayOfWeek(forecast.date),
            WeatherUtils().getResource(weatherForecast, time),
            WeatherUtils().convertWeatherType(forecast.weatherForecastDay.forecast)
        )
    }


    //SAVE TO DB

    private fun saveCityToDB(realm: Realm, city: City): Boolean {
        return realmModel.addCity(realm, City(city))
    }

    private fun saveHourForecastToDB(realm: Realm, hourForecast: HourForecast): Boolean {
        return realmModel.addHourForecast(
            realm,
            HourForecast(hourForecast)
        )
    }

    private fun saveDailyForecasts(realm: Realm, dailyForecasts: DailyForecasts): Boolean {
        var success: Boolean = true
        dailyForecasts.forecasts.forEach { forecast ->
            success = success && realmModel.addDayForecast(
                realm,
                DayForecast(forecast)
            )
        }
        return success
    }


    //LOAD FROM DB

    private fun loadDataFromDB(realm: Realm): Boolean {
        return if (loadCityFromDB(realm)) {
            if (loadHourForecastFromDB(realm)) {
                loadDailyForecastsFromDB(realm)
            } else {
                false
            }
        } else {
            false
        }
    }

    private fun loadCityFromDB(realm: Realm): Boolean {
        val cityDB: com.chizhikov.weatherapp.realmDB.City? = realmModel.getCity(realm)
        return if (cityDB != null) {
            val city = convertCity(cityDB)
            currentCityKey = city.locationKey
            currentCityName = "${city.englishName}, ${city.country.englishName}"
            true
        } else {
            false
        }
    }

    private fun loadHourForecastFromDB(realm: Realm): Boolean {
        val hourForecastDB: com.chizhikov.weatherapp.realmDB.HourForecast? =
            realmModel.getHourForecast(realm)
        return if (hourForecastDB != null) {
            updateHourForecast(convertHourForecast(hourForecastDB))
            true
        } else {
            false
        }
    }

    private fun loadDailyForecastsFromDB(realm: Realm): Boolean {
        val dailyForecastsDB: RealmResults<com.chizhikov.weatherapp.realmDB.DayForecast>? =
            realmModel.getDailyForecasts(realm)
        return if (dailyForecastsDB != null && dailyForecastsDB.size > 0) {
            val dailyForecasts = ArrayList<DailyForecastView>()
            dailyForecastsDB.forEach { forecastDB ->
                dailyForecasts.add(
                    convertForecastToView(
                        convertDayForecast(forecastDB),
                        dayOrNight()
                    )
                )
            }
            updateDailyForecast(dailyForecasts)
            true
        } else {
            false
        }
    }

    //CLEAR DB

    private fun clearDB(realm: Realm): Boolean {
        return realmModel.clearRealm(realm)
    }
}


