package com.example.weatherforecastscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.general_forecast_item.view.*
import kotlinx.android.synthetic.main.daily_forecast_item.view.*

class MainActivity : AppCompatActivity() {
    private var themeIsLight: Boolean = true
    private var currentTemperature: Int = 0
    private var currentWindFlow: Int = 0
    private var currentPerception: Int = 0
    private var currentHumidity: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeIsLight = savedInstanceState?.getBoolean(getString(R.string.RESTORE_THEME)) ?: true

        if (themeIsLight) {
            setTheme(R.style.LightTheme)
        } else {
            setTheme(R.style.DarkTheme)
        }

        setContentView(R.layout.activity_main)

        updateActivity()
    }


    fun onChangeThemeClick(v: View) {
        themeIsLight = !themeIsLight
        recreate()
    }


   private fun updateActivity() {
        if (themeIsLight) {
            imageButton.setImageResource(R.drawable.ic_theme_day)
        } else {
            imageButton.setImageResource(R.drawable.ic_theme_night)
        }

        currentTemperature = 9
        main_info_layout.main_forecast_image.setImageResource(R.drawable.forecast_image_cloudy)
        main_info_layout.current_temperature.text =
            "$currentTemperature" + getString(R.string.Celsius)
        main_info_layout.current_location.text = getString(R.string.SaintPetersburg)
        main_info_layout.current_weather_forecast.text = getString(R.string.forecast_cloudy)

        currentWindFlow = 78
        general_info_layout.general_forecast_item_1.general_item_layout.main_value.text =
            "$currentWindFlow"
        general_info_layout.general_forecast_item_1.general_item_layout.image_for_value.setImageResource(
            R.drawable.weather_windy
        )
        general_info_layout.general_forecast_item_1.general_item_layout.value_description.text =
            getString(
                R.string.WindFlow
            )

        currentPerception = 52
        general_info_layout.general_forecast_item_2.general_item_layout.main_value.text =
            "$currentPerception"
        general_info_layout.general_forecast_item_2.general_item_layout.image_for_value.setImageResource(
            R.drawable.weather_windy_variant
        )
        general_info_layout.general_forecast_item_2.general_item_layout.value_description.text =
            getString(
                R.string.Perception
            )

        currentHumidity = 89
        general_info_layout.general_forecast_item_3.general_item_layout.main_value.text =
            "$currentHumidity"
        general_info_layout.general_forecast_item_3.general_item_layout.image_for_value.setImageResource(
            R.drawable.weather_humidity
        )
        general_info_layout.general_forecast_item_3.general_item_layout.value_description.text =
            getString(
                R.string.Humidity
            )

        daily_info_scrollView.daily_info_layout.daily_forecast_item_1.daily_item_layout.day_value.text =
            getString(R.string.day_friday)
        daily_info_scrollView.daily_info_layout.daily_forecast_item_1.daily_item_layout.daily_forecast_item_image.setImageResource(
            R.drawable.weather_cloudy
        )
        daily_info_scrollView.daily_info_layout.daily_forecast_item_1.daily_item_layout.daily_forecast_description.text =
            getString(R.string.forecast_cloudy)


        daily_info_scrollView.daily_info_layout.daily_forecast_item_2.daily_item_layout.day_value.text =
            getString(R.string.day_saturday)
        daily_info_scrollView.daily_info_layout.daily_forecast_item_2.daily_item_layout.daily_forecast_item_image.setImageResource(
            R.drawable.weather_cloudy
        )
        daily_info_scrollView.daily_info_layout.daily_forecast_item_2.daily_item_layout.daily_forecast_description.text =
            getString(R.string.forecast_cloudy)

        daily_info_scrollView.daily_info_layout.daily_forecast_item_3.daily_item_layout.day_value.text =
            getString(R.string.day_sunday)
        daily_info_scrollView.daily_info_layout.daily_forecast_item_3.daily_item_layout.daily_forecast_item_image.setImageResource(
            R.drawable.weather_rainy
        )
        daily_info_scrollView.daily_info_layout.daily_forecast_item_3.daily_item_layout.daily_forecast_description.text =
            getString(R.string.forecast_rainy)

        daily_info_scrollView.daily_info_layout.daily_forecast_item_4.daily_item_layout.day_value.text =
            getString(R.string.day_Monday)
        daily_info_scrollView.daily_info_layout.daily_forecast_item_4.daily_item_layout.daily_forecast_item_image.setImageResource(
            R.drawable.weather_sunny
        )
        daily_info_scrollView.daily_info_layout.daily_forecast_item_4.daily_item_layout.daily_forecast_description.text =
            getString(R.string.forecast_sunny)

        daily_info_scrollView.daily_info_layout.daily_forecast_item_5.daily_item_layout.day_value.text =
            getString(R.string.day_tuesday)
        daily_info_scrollView.daily_info_layout.daily_forecast_item_5.daily_item_layout.daily_forecast_item_image.setImageResource(
            R.drawable.weather_windy_variant
        )
        daily_info_scrollView.daily_info_layout.daily_forecast_item_5.daily_item_layout.daily_forecast_description.text =
            getString(R.string.forecast_windy)

        daily_info_scrollView.daily_info_layout.daily_forecast_item_6.daily_item_layout.day_value.text =
            getString(
                R.string.day_wednesday
            )
        daily_info_scrollView.daily_info_layout.daily_forecast_item_6.daily_item_layout.daily_forecast_item_image.setImageResource(
            R.drawable.weather_sunny
        )
        daily_info_scrollView.daily_info_layout.daily_forecast_item_6.daily_item_layout.daily_forecast_description.text =
            getString(R.string.forecast_sunny)

        daily_info_scrollView.daily_info_layout.daily_forecast_item_7.daily_item_layout.day_value.text =
            getString(
                R.string.day_thursday
            )
        daily_info_scrollView.daily_info_layout.daily_forecast_item_7.daily_item_layout.daily_forecast_item_image.setImageResource(
            R.drawable.weather_sunny
        )
        daily_info_scrollView.daily_info_layout.daily_forecast_item_7.daily_item_layout.daily_forecast_description.text =
            getString(R.string.forecast_sunny)


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(getString(R.string.RESTORE_THEME), themeIsLight)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        themeIsLight = savedInstanceState.getBoolean(getString(R.string.RESTORE_THEME)  )
    }
}

