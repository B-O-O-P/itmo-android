package com.chizhikov.weatherapp.realmDB

import com.chizhikov.weatherapp.weatherApi.Temperature
import io.realm.RealmObject

open class Temperature(
    var value: Double = 0.0,
    var unit: String = ""
) : RealmObject() {
    constructor(temperature: Temperature) : this() {
        value = temperature.value
        unit = temperature.unit
    }
}