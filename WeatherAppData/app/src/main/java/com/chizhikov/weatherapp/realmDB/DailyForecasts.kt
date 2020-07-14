package com.chizhikov.weatherapp.realmDB

import com.chizhikov.weatherapp.weatherApi.DailyForecasts
import io.realm.RealmList
import io.realm.RealmObject

open class DailyForecasts(open var forecasts: RealmList<DayForecast> = RealmList()) :
    RealmObject() {
    constructor(dailyForecasts: DailyForecasts) : this() {
        dailyForecasts.forecasts.forEach { forecast ->
            forecasts.add(DayForecast(forecast))
        }
    }
}