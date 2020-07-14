package com.chizhikov.weatherapp.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.icu.util.TimeZone
import android.os.Build
import androidx.annotation.RequiresApi
import com.chizhikov.weatherapp.R

class WeatherUtils {
    private val weekDayArray =
        arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    @RequiresApi(Build.VERSION_CODES.N)
    fun getDayOfWeek(date: String): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+hh:mm")
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = df.parse(date)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return weekDayArray[dayOfWeek - 1]
    }

    fun getResource(weatherType: String, time: String): Int {
        if (time == "Day") {
            return when {
                weatherType.contains("windy", true) -> R.drawable.weather_windy_variant
                weatherType.contains("storm", true) -> R.drawable.forecast_image_thunder
                weatherType.contains("rain", true) -> R.drawable.forecast_image_rainy_6
                weatherType.contains("snow", true) -> R.drawable.forecast_image_snowy_5
                weatherType.contains("sunny", true) -> R.drawable.forecast_image_day
                else -> R.drawable.forecast_image_cloudy_day_3
            }
        } else {
            return when {
                weatherType.contains("windy", true) -> R.drawable.weather_windy_variant
                weatherType.contains("storm", true) -> R.drawable.forecast_image_thunder
                weatherType.contains("rain", true) -> R.drawable.forecast_image_rainy_6
                weatherType.contains("snow", true) -> R.drawable.forecast_image_snowy_5
                weatherType.contains("clear", true) -> R.drawable.forecast_image_day
                else -> R.drawable.ic_cloudy_night_3
            }
        }
    }

    fun convertWeatherType(weatherType: String): String {
        return when {
            weatherType.contains("windy", true) -> "Windy"
            weatherType.contains("storm", true) -> "Thunder"
            weatherType.contains("rain", true) -> "Rainy"
            weatherType.contains("snow", true) -> "Snowy"
            weatherType.contains("sunny", true) -> "Sunny"
            weatherType.contains("clear", true) -> "Clear"
            else -> "Cloudy"
        }
    }
}

