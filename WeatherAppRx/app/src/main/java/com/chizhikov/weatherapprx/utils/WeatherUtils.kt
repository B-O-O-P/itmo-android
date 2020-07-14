package com.chizhikov.weatherapprx.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import com.chizhikov.weatherapprx.R

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
                weatherType.contains("clear", true) -> R.drawable.forecast_image_night
                else -> R.drawable.forecast_image_cloudy_night_1
            }
        }
    }

    fun getResourceAnimated(weatherType: String, time: String): Int {
        if (time == "Day") {
            return when {
                weatherType.contains("windy", true) -> R.drawable.weather_windy_variant
                weatherType.contains("storm", true) -> R.drawable.forecast_animated_thunder
                weatherType.contains("rain", true) -> R.drawable.forecast_animated_rainy
                weatherType.contains("snow", true) -> R.drawable.forecast_animated_snowy
                weatherType.contains("sunny", true) -> R.drawable.forecast_animated_day
                else -> R.drawable.forecast_animated_cloudy_day
            }
        } else {
            return when {
                weatherType.contains("windy", true) -> R.drawable.weather_windy_variant
                weatherType.contains("storm", true) -> R.drawable.forecast_animated_thunder
                weatherType.contains("rain", true) -> R.drawable.forecast_animated_rainy
                weatherType.contains("snow", true) -> R.drawable.forecast_animated_snowy
                weatherType.contains("clear", true) -> R.drawable.forecast_animated_night
                else -> R.drawable.forecast_animated_cloudy_night
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

