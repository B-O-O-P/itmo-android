package com.chizhikov.weatherapp.realmDB

import com.chizhikov.weatherapp.weatherApi.HourForecast
import io.realm.RealmObject

open class Speed(
    var value: Double = 0.0,
    var unit: String = ""
) : RealmObject() {
    constructor(speed: HourForecast.Wind.Speed) : this() {
        value = speed.value
        unit = speed.unit
    }
}