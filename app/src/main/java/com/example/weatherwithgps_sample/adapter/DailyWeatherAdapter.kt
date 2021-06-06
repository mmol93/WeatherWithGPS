package com.example.weatherwithgps_sample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherwithgps_sample.R
import com.example.weatherwithgps_sample.databinding.DailyRowBinding

class DailyWeatherAdapter(val context: Context,
                          val weekList : ArrayList<String>,
                          val rainPercentList : ArrayList<Int>,
                          val weatherMainList : ArrayList<String>,
                          val minTempList : ArrayList<Int>,
                          val maxTempList : ArrayList<Int>)
    : RecyclerView.Adapter<DailyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder(LayoutInflater.from(context).inflate(R.layout.daily_row, parent, false))
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return weekList.size
    }
}
class DailyViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val binder : DailyRowBinding = DailyRowBinding.bind(view)

    val row_dayWeek = binder.dateWeek
    val row_rainImage = binder.rainPercentImageView
    val row_rainText = binder.rainPercentTextView
    val row_weatherImage = binder.weatherImageView
    val row_minMaxText = binder.minMaxTextView
}