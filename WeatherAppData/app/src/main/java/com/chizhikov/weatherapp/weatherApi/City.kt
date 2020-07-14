package com.chizhikov.weatherapp.weatherApi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class City(
    @Json(name = "EnglishName") val englishName: String,
    @Json(name = "Key") val locationKey: Int,
    @Json(name = "Country") val country: Country
) {
    data class Country(
        @Json(name = "ID") val id: String,
        @Json(name = "EnglishName") val englishName: String
    )
}