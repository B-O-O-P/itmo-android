package com.chizhikov.weatherapprx.dailyList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chizhikov.weatherapprx.R

class DailyForecastListAdapter(
    var forecasts: List<DailyForecastView>

) : RecyclerView.Adapter<DailyForecastViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        return DailyForecastViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.daily_forecast_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return forecasts.size
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.day.text = forecasts[position].dayOfWeek
        holder.weatherIcon.setImageResource(forecasts[position].weatherIcon)
        holder.weatherType.text = forecasts[position].weatherDescription
    }
}