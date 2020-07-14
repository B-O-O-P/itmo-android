package com.chizhikov.weatherapprx.weatherApi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourForecast(
    @Json(name = "IconPhrase") val weatherForecast: String,
    @Json(name = "Temperature") val temperature: Temperature,
    @Json(name = "Wind") val wind: Wind,
    @Json(name = "PrecipitationProbability") val precipitation: Int,
    @Json(name = "RelativeHumidity") val humidity: Int
) {
    data class Wind(
        @Json(name = "Speed") val speed: Speed
    ) {
        data class Speed(
            @Json(name = "Value") val value: Double,
            @Json(name = "Unit") val unit: String
        )
    }
}
