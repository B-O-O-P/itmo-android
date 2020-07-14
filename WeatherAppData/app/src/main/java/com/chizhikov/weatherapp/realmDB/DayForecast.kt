package com.chizhikov.weatherapp.realmDB

import com.chizhikov.weatherapp.weatherApi.DayForecast
import io.realm.RealmObject

open class DayForecast(
    var date: String = "",
    var weatherForecastDay: Day? = Day(),
    var weatherForecastNight: Night? = Night(),
    var temperatureStatistic: TemperatureStatistic? = TemperatureStatistic()
) : RealmObject() {
    constructor(dayForecast: DayForecast) : this() {
        date = dayForecast.date
        weatherForecastDay = Day(dayForecast.weatherForecastDay)
        weatherForecastNight = Night(dayForecast.weatherForecastNight)
        temperatureStatistic = TemperatureStatistic(dayForecast.temperatureStatistic)
    }
}