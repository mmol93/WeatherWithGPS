package com.example.weatherwithgps_sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import com.example.weatherwithgps_sample.adapter.WeatherAdapter
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

        refreshWeather()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binder.refreshImageView.setOnClickListener {
            binder.progressBar.isGone = false
            binder.refreshImageView.isGone = true
            refreshWeather()
            Log.d("test", "새로고침")
        }
    }

    fun refreshWeather(){
        // 현재 날짜 및 시간 가져오기
        val sdf = SimpleDateFormat("yyyy-MM-dd, HH:mm", Locale.getDefault())
        val hourFormat = SimpleDateFormat("HH", Locale.getDefault())
        val minFormat = SimpleDateFormat("mm", Locale.getDefault())
        val now = sdf.format(Date())
        // 현재 시간에서 3시간을 더한다
        var hourlyHour = System.currentTimeMillis() + (3*60*60*1000)
        // hourly에 사용될 시간 간격은 3시간이므로 3시간 씩 더한 시간을 리스트로 만든다
        for (i in 1..10){
            App.hour.add(hourFormat.format(hourlyHour))
            hourlyHour += (3 * 60 * 60 * 1000)
        }
        Log.d("test", "App.hour: ${App.hour}")

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
                binder.dataTextView.text = "${App.cityName}   "
                binder.dataTextView.append(now)
                binder.mainTextView.text = Weather.main
                binder.tempTextView.text = Weather.temp.toString() + "℃"
                binder.minMaxTextView.text = Weather.min_temp.toString() + "℃ / "
                binder.minMaxTextView.append("${Weather.max_temp}℃")
                binder.humidityPercentTextView.text = Weather.humility.toString() + "%"
            })

        // weather forecast API 데이터 호출
        RetrofitManager.instance.getForecast("${App.lat}", "${App.lon}", "hourly.temp", API.ID,
            completion = { hourlyTemp, hourlyPop, hourlyWind, hourlyUvi, hourlyMain ->
                // 결과 값 로그 출력
                Log.d("retrofit2", "from UI level, hourlyTemp: $hourlyTemp")
                Log.d("retrofit2", "from UI level, hourlyPop: $hourlyPop")
                Log.d("retrofit2", "from UI level, hourlyWind: $hourlyWind")
                Log.d("retrofit2", "from UI level, hourlyUvi: $hourlyUvi")
                Log.d("retrofit2", "from UI level, hourlyMain: $hourlyMain")

                // 시간별 데이터를 리사이클러 어댑터에 보내기
                val hourlyAdapter = WeatherAdapter(requireContext(),hourlyTemp, hourlyPop, hourlyMain)
                binder.hourlyRecycler.layoutManager =
                    GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
                binder.hourlyRecycler.adapter = hourlyAdapter

                // OneCall API에서 얻어서 뷰에 넣는 경우 여기에 정의한다
                binder.rainPercentTextView.text = Weather.rainPercent.toString() + "%"
                binder.windPercentTextView.text = "${Weather.wind}m/s"
                binder.feelTextView.text = "체감온도: " + Weather.feels.toString() + "℃"
                binder.UVpercentTextView.text = Weather.uvi
                binder.progressBar.isGone = true
                binder.refreshImageView.isGone = false
            })
    }
}