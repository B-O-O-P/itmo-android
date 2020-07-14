package com.chizhikov.weatherapprx.weatherApi

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


interface AccuWeatherApi {
    @GET("locations/v1/cities/{countryID}/search")
    fun getCity(@Path("countryID") countryID: String, @Query("apikey") apiKey: String, @Query("q") query: String): Call<List<City>>

    @GET("forecasts/v1/hourly/1hour/{locationKey}")
    fun getHourForecast(
        @Path("locationKey") locationKey: Int, @Query("apikey") apiKey: String, @Query(
            "details"
        ) details: Boolean, @Query("metric") metric: Boolean
    ): Call<List<HourForecast>>

    @GET("forecasts/v1/daily/5day/{locationKey}")
    fun getDailyForecasts(
        @Path("locationKey") locationKey: Int, @Query("apikey") apiKey: String, @Query(
            "metric"
        ) metric: Boolean
    ): Call<DailyForecasts>
}

fun createAccuWeatherApi(): AccuWeatherApi {
    val client = OkHttpClient()
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://dataservice.accuweather.com/")
        .build()
    return retrofit.create(AccuWeatherApi::class.java)
}