package com.chizhikov.weatherapp.realmDB

import com.chizhikov.weatherapp.weatherApi.DayForecast
import io.realm.RealmObject

open class Day(var forecast: String = "") : RealmObject() {
    constructor(day: DayForecast.Day) : this() {
        forecast = day.forecast
    }
}
