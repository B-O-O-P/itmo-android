package com.chizhikov.weatherapp.realmDB

import io.realm.RealmObject

open class TimeDB(var value: Long = 0) : RealmObject()