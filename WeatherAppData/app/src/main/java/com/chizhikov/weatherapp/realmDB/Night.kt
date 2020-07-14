package com.chizhikov.weatherapp.realmDB

import com.chizhikov.weatherapp.weatherApi.DayForecast
import io.realm.RealmObject

open class Night(var forecast: String = "") : RealmObject() {
    constructor(night: DayForecast.Night) : this() {
        forecast = night.forecast
    }
}