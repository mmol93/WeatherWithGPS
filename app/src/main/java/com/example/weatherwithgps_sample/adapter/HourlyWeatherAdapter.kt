package com.example.weatherwithgps_sample.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherwithgps_sample.App
import com.example.weatherwithgps_sample.R
import com.example.weatherwithgps_sample.databinding.HourlyRowBinding

class HourlyWeatherAdapter(val context : Context,
                           val hourlyTemp : ArrayList<Long>, val hourlyPop : ArrayList<Double>, val hourlyMain : ArrayList<String>)
    : RecyclerView.Adapter<HourlyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return HourlyViewHolder(LayoutInflater.from(context).inflate(R.layout.hourly_row, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holderHourly: HourlyViewHolder, position: Int) {
        holderHourly.row_tempText.text = "${hourlyTemp[position]}℃"
        holderHourly.row_rainText.text = "${hourlyPop[position].toInt()}%"
        holderHourly.row_timeText.text = "${App.hour[position]}:00"
    }

    override fun getItemCount(): Int {
        return hourlyTemp.size
    }
}

class HourlyViewHolder(view : View) : RecyclerView.ViewHolder(view){
    val binder : HourlyRowBinding = HourlyRowBinding.bind(view)

    val row_tempText = binder.tempTextView
    val row_weatherImage = binder.weatherImageView
    val row_rainText = binder.rainPercentTextView
    val row_timeText = binder.timeTextView
}