package com.chizhikov.weatherapp.realmDB

import com.chizhikov.weatherapp.weatherApi.DayForecast
import io.realm.RealmObject

open class TemperatureStatistic(
    var max: Temperature? = Temperature(),
    var min: Temperature? = Temperature()
) : RealmObject() {
    constructor(temperatureStatistic: DayForecast.TemperatureStatistic) : this() {
        max = Temperature(temperatureStatistic.max)
        min = Temperature(temperatureStatistic.min)
    }
}