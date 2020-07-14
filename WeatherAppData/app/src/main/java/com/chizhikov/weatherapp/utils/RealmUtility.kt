package com.chizhikov.weatherapp.utils

import io.realm.RealmConfiguration


object RealmUtility {
    private val SCHEMA_V_PREV = 1// previous schema version
    private val schemaVNow = 2// change schema version if any change happened in schema


    // if migration needed then this methoud will remove the existing database and will create new database
    val defaultConfig: RealmConfiguration
        get() = RealmConfiguration.Builder()
            .schemaVersion(schemaVNow.toLong())
            .deleteRealmIfMigrationNeeded()
            .build()
}