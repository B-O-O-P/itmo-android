package com.chizhikov.weatherapprx

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.chizhikov.weatherapprx.dailyList.DailyForecastListAdapter
import com.chizhikov.weatherapprx.dailyList.DailyForecastView
import com.chizhikov.weatherapprx.utils.WeatherUtils
import com.chizhikov.weatherapprx.weatherApi.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.general_forecast_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val LOG_TAG = "Weather API"

@RequiresApi(Build.VERSION_CODES.N)
class MainActivity : AppCompatActivity() {
    private val API_KEY = BuildConfig.API_KEY
    private var themeIsLight: Boolean = true
    private var currentCityKey: Int? = null
    private var currentCityName: String = ""
    private lateinit var currentForecast: Forecast
    private lateinit var adapter: DailyForecastListAdapter
    private var listState: Parcelable? = null

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
        if (savedInstanceState == null) {
            setCurrentLocationAndForecast("RU", "Saint Petersburg")
        } else {
            currentForecast = (lastCustomNonConfigurationInstance as? Forecast)!!
            currentCityName = savedInstanceState.getString("CITY_NAME").toString()
            setGeneralForecast(
                currentForecast.currentForecast,
                currentForecast.currentTemperature,
                currentForecast.currentWindSpeed,
                currentForecast.currentPrecipitation,
                currentForecast.currentHumidity
            )
            updateDailyForecast(currentForecast.currentDailyList)
            daily_info_list.layoutManager?.onRestoreInstanceState(listState)
        }
        updateActivity()
        setFullscreen()

        main_refresh.setOnRefreshListener {
            main_refresh.isRefreshing = true
            setHourForecast(currentCityKey, true, true)
            setDailyForecast(currentCityKey, true)
            main_refresh.isRefreshing = false
        }
    }


    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return currentForecast
    }

    fun onChangeThemeClick(v: View) {
        themeIsLight = !themeIsLight
        recreate()
    }

    private fun getTime(): String {
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

    private fun updateActivity() {
        if (themeIsLight) {
            imageButton.setImageResource(R.drawable.ic_theme_day)
        } else {
            imageButton.setImageResource(R.drawable.ic_theme_night)
        }
    }

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

    private fun setCurrentLocationAndForecast(countryID: String, query: String) {
        val cityCall: Call<List<City>> =
            WeatherAppRx.app.accuWeatherApi.getCity(countryID, API_KEY, query)
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
                }
                setHourForecast(currentCityKey, true, true)
                setDailyForecast(currentCityKey, true)
            }
        })
    }

    private fun setHourForecast(locationKey: Int?, details: Boolean, metric: Boolean) {
        if (locationKey != null) {
            val hourCall: Call<List<HourForecast>> =
                WeatherAppRx.app.accuWeatherApi.getHourForecast(locationKey, API_KEY, details, metric)
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
                        updateCurrentForecast(
                            forecast.weatherForecast,
                            currentTemperature,
                            currentWindValue,
                            currentPrecipitation,
                            currentHumidity
                        )
                        setGeneralForecast(
                            forecast.weatherForecast,
                            currentTemperature,
                            currentWindValue,
                            currentPrecipitation,
                            currentHumidity
                        )
                    }
                }

            })
        }
    }

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

    private fun updateCurrentForecast(
        mainForecast: String,
        temperature: String,
        wind: String,
        precipitation: String,
        humidity: String
    ) {
        currentForecast = Forecast(
            mainForecast, temperature, wind, precipitation,
            humidity, adapter.forecasts
        )
    }

    private fun setGeneralForecast(
        mainForecast: String,
        temperature: String,
        wind: String,
        precipitation: String,
        humidity: String
    ) {
        current_location.text = currentCityName
        current_weather_forecast.text = WeatherUtils().convertWeatherType(mainForecast)
        main_forecast_image.setImageResource(WeatherUtils().getResourceAnimated(mainForecast, getTime()))

        current_temperature.text = temperature

        general_forecast_item_1.main_value.text = wind
        general_forecast_item_2.main_value.text = precipitation
        general_forecast_item_3.main_value.text = humidity
    }

    private fun setDailyForecast(locationKey: Int?, metric: Boolean) {
        if (locationKey != null) {
            val dailyCall: Call<DailyForecasts> =
                WeatherAppRx.app.accuWeatherApi.getDailyForecasts(
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
                        dailyForecastListView.add(convertForecastToView(it, getTime()))
                    }
                    updateDailyForecast(dailyForecastListView)
                }
            })
        }
    }

    private fun initDailyForecasts() {
        adapter = DailyForecastListAdapter(emptyList())
        daily_info_list.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@MainActivity.adapter
        }
        currentForecast = Forecast("", "", "", "", "", adapter.forecasts)
    }

    private fun updateDailyForecast(currentDailyForecasts: List<DailyForecastView>) {
        adapter.forecasts = currentDailyForecasts
        adapter.notifyDataSetChanged()
        currentForecast.currentDailyList = currentDailyForecasts
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

    private class Forecast(
        var currentForecast: String,
        var currentTemperature: String,
        var currentWindSpeed: String,
        var currentPrecipitation: String,
        var currentHumidity: String,
        var currentDailyList: List<DailyForecastView>
    )
}


