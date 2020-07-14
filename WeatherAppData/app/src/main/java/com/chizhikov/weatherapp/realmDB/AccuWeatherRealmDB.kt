package com.chizhikov.weatherapp.realmDB

import io.realm.Realm
import io.realm.RealmResults

interface AccuWeatherRealmDB {
    fun addTime(realm: Realm, time: Long): Boolean

    fun addDayForecast(realm: Realm, dayForecast: DayForecast): Boolean
    fun addHourForecast(realm: Realm, hourForecast: HourForecast): Boolean
    fun addCity(realm: Realm, city: City): Boolean

    fun getTime(realm: Realm): TimeDB?

    fun getDailyForecasts(realm: Realm): RealmResults<DayForecast>?
    fun getCity(realm: Realm): City?
    fun getHourForecast(realm: Realm): HourForecast?

    fun clearRealm(realm: Realm): Boolean
}