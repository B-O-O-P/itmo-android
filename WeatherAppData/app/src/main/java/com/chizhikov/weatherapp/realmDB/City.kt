package com.chizhikov.weatherapp.realmDB

import com.chizhikov.weatherapp.weatherApi.City
import io.realm.RealmObject

open class City(
    var englishName: String = "",
    var locationKey: Int = 0,
    var country: Country? = Country()
) : RealmObject() {
    constructor(city: City) : this() {
        englishName = city.englishName
        locationKey = city.locationKey
        country = Country(city.country)
    }
}