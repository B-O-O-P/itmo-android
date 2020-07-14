package com.chizhikov.weatherapp.realmDB

import com.chizhikov.weatherapp.weatherApi.HourForecast
import io.realm.RealmObject

open class HourForecast(
    var weatherForecast: String = "",
    var temperature: Temperature? = Temperature(),
    var wind: Wind? = Wind(),
    var precipitation: Int = 0,
    var humidity: Int = 0
) : RealmObject() {
    constructor(hourForecast: HourForecast) : this() {
        weatherForecast = hourForecast.weatherForecast
        temperature = Temperature(hourForecast.temperature)
        wind = Wind(hourForecast.wind)
        precipitation = hourForecast.precipitation
        humidity = hourForecast.humidity
    }
}
