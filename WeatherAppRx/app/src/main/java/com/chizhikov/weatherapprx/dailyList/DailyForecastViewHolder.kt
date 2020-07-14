package com.chizhikov.weatherapprx.dailyList

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.daily_forecast_item.view.*

class DailyForecastViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    val weatherIcon: ImageView = root.daily_forecast_icon
    val day: TextView = root.day
    val weatherType: TextView = root.daily_forecast_type
}