package com.chizhikov.weatherapp.weatherApi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyForecasts(@Json(name = "DailyForecasts") val forecasts: List<DayForecast>)