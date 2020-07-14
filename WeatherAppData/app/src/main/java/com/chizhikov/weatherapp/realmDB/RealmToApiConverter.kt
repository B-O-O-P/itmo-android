package com.chizhikov.weatherapp.realmDB

import com.chizhikov.weatherapp.weatherApi.City
import com.chizhikov.weatherapp.weatherApi.DayForecast
import com.chizhikov.weatherapp.weatherApi.HourForecast

fun convertCountry(country: Country?): City.Country {
    return if (country != null) {
        City.Country(country.id, country.englishName)
    } else {
        City.Country("", "")
    }
}

fun convertCity(city: com.chizhikov.weatherapp.realmDB.City): City {
    return City(
        city.englishName,
        city.locationKey,
        convertCountry(city.country)
    )
}

fun convertDay(day: Day?): DayForecast.Day {
    return if (day != null) {
        DayForecast.Day(day.forecast)
    } else {
        DayForecast.Day("")
    }
}

fun convertNight(night: Night?): DayForecast.Night {
    return if (night != null) {
        DayForecast.Night(night.forecast)
    } else {
        DayForecast.Night("")
    }
}

fun convertSpeed(speed: Speed?): HourForecast.Wind.Speed {
    return if (speed != null) {
        HourForecast.Wind.Speed(speed.value, speed.unit)
    } else {
        HourForecast.Wind.Speed(0.0, "")
    }
}

fun convertWind(wind: Wind?): HourForecast.Wind {
    return if (wind != null) {
        HourForecast.Wind(convertSpeed(wind.speed))
    } else {
        HourForecast.Wind(convertSpeed(Speed(0.0, "")))
    }
}

fun convertTemperature(temperature: Temperature?): com.chizhikov.weatherapp.weatherApi.Temperature {
    return if (temperature != null) {
        com.chizhikov.weatherapp.weatherApi.Temperature(temperature.value, temperature.unit)
    } else {
        com.chizhikov.weatherapp.weatherApi.Temperature(0.0, "")
    }
}

fun convertHourForecast(forecast: com.chizhikov.weatherapp.realmDB.HourForecast): HourForecast {
    return HourForecast(
        forecast.weatherForecast, convertTemperature(forecast.temperature),
        convertWind(forecast.wind), forecast.precipitation, forecast.humidity
    )
}

fun convertTemperatureStatistic(temperatureStatistic: TemperatureStatistic?): DayForecast.TemperatureStatistic {
    return if (temperatureStatistic != null) {
        DayForecast.TemperatureStatistic(
            convertTemperature(temperatureStatistic.max),
            convertTemperature(temperatureStatistic.min)
        )
    } else {
        DayForecast.TemperatureStatistic(
            com.chizhikov.weatherapp.weatherApi.Temperature(
                0.0,
                ""
            ), com.chizhikov.weatherapp.weatherApi.Temperature(0.0, "")
        )
    }
}

fun convertDayForecast(dayForecast: com.chizhikov.weatherapp.realmDB.DayForecast): DayForecast {
    return com.chizhikov.weatherapp.weatherApi.DayForecast(
        dayForecast.date,
        convertDay(dayForecast.weatherForecastDay),
        convertNight(dayForecast.weatherForecastNight),
        convertTemperatureStatistic(dayForecast.temperatureStatistic)
    )
}
