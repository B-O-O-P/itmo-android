package com.chizhikov.weatherapp.utils

import java.util.*
import kotlin.math.abs

fun getCurrentTime(): Date {
    return Calendar.getInstance().time
}

fun convertMillisToMinutes(millis: Long): Long {
    return millis / 60000
}

fun findDiffInMinutes(firstDate: Long, secondDate: Long): Long {
    return convertMillisToMinutes(abs(firstDate - secondDate))
}