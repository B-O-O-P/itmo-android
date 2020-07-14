package com.chizhikov.weatherapp.realmDB

import android.util.Log
import io.realm.Realm
import io.realm.RealmResults
import java.lang.Exception

const val LOG_TAG = "Accuweather Realm model"

class AccuWeatherRealmModel : AccuWeatherRealmDB {
    override fun addTime(realm: Realm, time: Long): Boolean {
        return try {
            realm.beginTransaction()
            realm.copyToRealm(TimeDB(time))
            realm.commitTransaction()
            true
        } catch (e: Exception) {
            Log.d(LOG_TAG, e.message)
            false
        }
    }

    override fun getTime(realm: Realm): TimeDB? {
        return realm.where(TimeDB::class.java).findFirst()
    }

    override fun addDayForecast(realm: Realm, dayForecast: DayForecast): Boolean {
        return try {
            realm.beginTransaction()
            realm.copyToRealm(dayForecast)
            realm.commitTransaction()
            true
        } catch (e: Exception) {
            Log.d(LOG_TAG, e.message)
            false
        }
    }

    override fun addHourForecast(realm: Realm, hourForecast: HourForecast): Boolean {
        return try {
            realm.beginTransaction()
            realm.copyToRealm(hourForecast)
            realm.commitTransaction()
            true
        } catch (e: Exception) {
            Log.d(LOG_TAG, e.message)
            false
        }
    }

    override fun addCity(realm: Realm, city: City): Boolean {
        return try {
            realm.beginTransaction()
            realm.copyToRealm(city)
            realm.commitTransaction()
            true
        } catch (e: Exception) {
            Log.d(LOG_TAG, e.message)
            false
        }
    }

    override fun getDailyForecasts(realm: Realm): RealmResults<DayForecast>? {
        return realm.where(DayForecast::class.java).findAll()
    }

    override fun getCity(realm: Realm): City? {
        return realm.where(City::class.java).findFirst()
    }

    override fun getHourForecast(realm: Realm): HourForecast? {
        return realm.where(HourForecast::class.java).findFirst()
    }


    override fun clearRealm(realm: Realm): Boolean {
        return try {
            realm.beginTransaction()
            realm.deleteAll()
            realm.commitTransaction()
            true
        } catch (e: Exception) {
            Log.d(LOG_TAG, e.message)
            false
        }
    }

}