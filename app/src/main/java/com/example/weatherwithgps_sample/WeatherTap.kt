package com.example.weatherwithgps_sample

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.weatherwithgps_sample.databinding.FragmentWeatherTapBinding
import com.example.weatherwithgps_sample.retrofit.RetrofitManager

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // API 호출
        val locationInfo = "${App.cityName}, ${App.stateName}, ${App.countryName}"
        RetrofitManager.instance.getCurrentWeatherData(locationInfo, API.ID,
        completion = {
            // 결과 값 출력하기
            Toast.makeText(App.context, "통신완료", Toast.LENGTH_SHORT).show()
            Log.d("retrofit", "cityName: ${App.cityName}")
            Log.d("retrofit", "stateName: ${App.stateName}")
            Log.d("retrofit", "countryName: ${App.countryName}")
            Log.d("retrofit", "main: ${Weather.main}")
            Log.d("retrofit", "description: ${Weather.description}")
            Log.d("retrofit", "temp: ${Weather.temp}")
            Log.d("retrofit", "max_temp: ${Weather.max_temp}")
            Log.d("retrofit", "min_temp: ${Weather.min_temp}")
            Log.d("retrofit", "feels: ${Weather.feels}")
            Log.d("retrofit", "humility: ${Weather.humility}")
            Log.d("retrofit", "wind: ${Weather.wind}")
        })
    }
}