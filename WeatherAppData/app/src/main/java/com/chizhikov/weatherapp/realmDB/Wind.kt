package com.chizhikov.weatherapp.realmDB

import com.chizhikov.weatherapp.weatherApi.HourForecast
import io.realm.RealmObject

open class Wind(
    var speed: Speed? = Speed()
) : RealmObject() {
    constructor(wind: HourForecast.Wind) : this() {
        speed = Speed(wind.speed)
    }
}