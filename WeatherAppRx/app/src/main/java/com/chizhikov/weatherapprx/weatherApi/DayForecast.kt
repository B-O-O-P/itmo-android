package com.chizhikov.weatherapprx.weatherApi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DayForecast(
    @Json(name = "Date") val date: String,
    @Json(name = "Day") val weatherForecastDay: Day,
    @Json(name = "Night") val weatherForecastNight: Night,
    @Json(name = "Temperature") val temperatureStatistic: TemperatureStatistic
) {
    data class TemperatureStatistic(
        @Json(name = "Maximum") val max: Temperature,
        @Json(name = "Minimum") val min: Temperature
    )

    data class Day(@Json(name = "IconPhrase") val forecast: String)

    data class Night(@Json(name = "IconPhrase") val forecast: String)

}