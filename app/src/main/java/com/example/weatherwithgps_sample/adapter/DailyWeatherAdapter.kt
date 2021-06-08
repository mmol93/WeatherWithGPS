package com.example.weatherwithgps_sample.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherwithgps_sample.Calendar
import com.example.weatherwithgps_sample.R
import com.example.weatherwithgps_sample.databinding.DailyRowBinding

class DailyWeatherAdapter(val context: Context,
                          val rainPercentList : ArrayList<Int>,
                          val dailyMainList : ArrayList<String>,
                          val minTempList : ArrayList<Int>,
                          val maxTempList : ArrayList<Int>)
    : RecyclerView.Adapter<DailyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder(LayoutInflater.from(context).inflate(R.layout.daily_row, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val weekList = Calendar.getWeek()
        Log.d("test", "weekList: $weekList")
        holder.row_dayWeek.text = weekList[position]
        holder.row_rainText.text = rainPercentList[position].toString() + "%"
        holder.row_minMaxText.text = minTempList[position].toString() + "℃ / "
        holder.row_minMaxText.append(maxTempList[position].toString() + "℃")
        when(dailyMainList[position]){
            "Thunderstorm" -> holder.row_weatherImage.setImageResource(R.drawable.ic_thunder)
            "Drizzle" -> holder.row_weatherImage.setImageResource(R.drawable.ic_little_rain)
            "Rain" -> {
                holder.row_weatherImage.setImageResource(R.drawable.ic_rain)
                if (rainPercentList[position] in 40..50){
                    holder.row_weatherImage.setImageResource(R.drawable.ic_little_rain)
                }
            }
            "Snow" -> holder.row_weatherImage.setImageResource(R.drawable.ic_snow)
            "Clear" -> holder.row_weatherImage.setImageResource(R.drawable.ic_sunny)
            "Clouds" -> holder.row_weatherImage.setImageResource(R.drawable.ic_clouds)
            "Mist", "Dust", "Fog", "Haze", "Sand", "Ash" -> holder.row_weatherImage.setImageResource(R.drawable.ic_fog)
            "Tornado", "Squall" -> holder.row_weatherImage.setImageResource(R.drawable.ic_tornado)
        }
    }

    override fun getItemCount(): Int {
        return rainPercentList.size
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