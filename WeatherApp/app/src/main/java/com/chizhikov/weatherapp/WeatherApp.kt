package com.chizhikov.weatherapp

import android.app.Application
import com.chizhikov.weatherapp.weatherApi.AccuWeatherApi
import com.chizhikov.weatherapp.weatherApi.createAccuWeatherApi

class WeatherApp : Application() {
    lateinit var accuWeatherApi: AccuWeatherApi
        private set

    override fun onCreate() {
        super.onCreate()
        val api = createAccuWeatherApi()
        accuWeatherApi = api
        app = this
    }

    companion object {
        lateinit var app: WeatherApp
            private set
    }
}