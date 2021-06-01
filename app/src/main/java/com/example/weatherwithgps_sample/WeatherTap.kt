package com.example.weatherwithgps_sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.weatherwithgps_sample.databinding.FragmentWeatherTapBinding
import com.example.weatherwithgps_sample.retrofit.RetrofitManager
import java.text.SimpleDateFormat
import java.util.*

class WeatherTap : Fragment() {
    lateinit var binder : FragmentWeatherTapBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather_tap, container, false)
        binder = FragmentWeatherTapBinding.bind(view)
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 현재 날짜 가져오기
        val sdf = SimpleDateFormat("yyyy-MM-dd, HH:mm", Locale.getDefault())
        val now = sdf.format(Date())

        // current weather API 호출
        val locationInfo = "${App.cityName}, ${App.stateCode}, ${App.countryCode}"
        RetrofitManager.instance.getCurrentWeatherData(locationInfo, API.ID,
        completion = {
            // 결과 값 출력하기
            Toast.makeText(App.context, "통신완료", Toast.LENGTH_SHORT).show()
            Log.d("retrofit", "locationInfo: $locationInfo")
            Log.d("retrofit", "cityName: ${App.cityName}")
            Log.d("retrofit", "stateCode: ${App.stateCode}")
            Log.d("retrofit", "stateName: ${App.stateName}")
            Log.d("retrofit", "countryCode: ${App.countryCode}")
            Log.d("retrofit", "main: ${Weather.main}")
            Log.d("retrofit", "description: ${Weather.description}")
            Log.d("retrofit", "temp: ${Weather.temp}")
            Log.d("retrofit", "max_temp: ${Weather.max_temp}")
            Log.d("retrofit", "min_temp: ${Weather.min_temp}")
            Log.d("retrofit", "feels: ${Weather.feels}")
            Log.d("retrofit", "humility: ${Weather.humility}")
            Log.d("retrofit", "wind: ${Weather.wind}")
            Log.d("retrofit", "sunRise: ${Weather.sunRise}")
            Log.d("retrofit", "sunSet: ${Weather.sunSet}")
            Log.d("retrofit", "country: ${Weather.country}")

            // 결과를 뷰에 적용하기
            binder.cityNameTextView.text = "${App.cityName}, "
            binder.cityNameTextView.append(App.stateName)
            binder.dateTextView.text = now
            binder.weatherTextView.text = Weather.main
            binder.tempTextView.text = Weather.temp.toString() + "℃"
            binder.tempMax.text = Weather.max_temp.toString() + "℃"
            binder.tempMin.text = Weather.min_temp.toString() + "℃"
            binder.sunrise.text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(Weather.sunRise*1000))
            binder.sunset.text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(Weather.sunSet*1000))
            binder.wind.text = "${Weather.wind}m/s"
            binder.feelsLike.text = Weather.feels.toString() + "℃"
            binder.humidity.text = Weather.humility.toString()
        })

        // weather forecast API 데이터 호출
        RetrofitManager.instance.getForecast("${App.lat}", "${App.lon}", "hourly.temp", API.ID,
        completion = {
            // 결과 값 출력

        })
    }
}