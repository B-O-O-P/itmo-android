package com.chizhikov.weatherapprx.weatherApi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Temperature(
    @Json(name = "Value") val value: Double,
    @Json(name = "Unit") val unit: String
)