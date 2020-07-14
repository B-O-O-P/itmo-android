package com.chizhikov.weatherapprx

import android.app.Application
import com.chizhikov.weatherapprx.weatherApi.AccuWeatherApi
import com.chizhikov.weatherapprx.weatherApi.createAccuWeatherApi

class WeatherAppRx : Application() {
    lateinit var accuWeatherApi: AccuWeatherApi
        private set

    override fun onCreate() {
        super.onCreate()
        val api = createAccuWeatherApi()
        accuWeatherApi = api
        app = this
    }

    companion object {
        lateinit var app: WeatherAppRx
            private set
    }
}