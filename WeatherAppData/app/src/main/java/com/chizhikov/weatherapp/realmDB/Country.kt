package com.chizhikov.weatherapp.realmDB

import com.chizhikov.weatherapp.weatherApi.City
import io.realm.RealmObject

open class Country(
    var id: String = "",
    var englishName: String = ""
) : RealmObject() {
    constructor(country: City.Country) : this() {
        id = country.id
        englishName = country.englishName
    }
}