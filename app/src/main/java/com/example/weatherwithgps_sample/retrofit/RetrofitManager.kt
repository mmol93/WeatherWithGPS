package com.example.weatherwithgps_sample.retrofit

import android.util.Log
import com.example.weatherwithgps_sample.API
import com.example.weatherwithgps_sample.Weather
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {
    companion object{
        val instance = RetrofitManager()
    }
    // 레트로핏 인터페이스 가져오기
    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.baseURL)?.create(IRetrofit::class.java)

    // 날씨 데이터를 가져오는 API 호출
    fun getWeatherData(locationInfo : String, AppId : String,
                       completion:(ArrayList<String>) -> Unit){
        val call = iRetrofit?.getWeatherData(locationInfo, AppId) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit", "접속 성공\n, response: ${response.body()}, code: ${response.code()}")
                when(response.code()){
                    200 ->{
                        val weatherDataList = ArrayList<String>()
                        // response.body()에 어떤 값이 있을 경우
                        response.body().let {
                            // 1차: JsonObject 분해
                            val body = it!!.asJsonObject
                            // 2차: 원하는 JsonArray 가져와서 JsonObject로 분해
                            val weatherArray = body.getAsJsonArray("weather")
                            val weatherData = weatherArray.asJsonObject
                            // 3차 JsonObject 가져왔으면 그 안에서 필요한 요소 다시 가져오기
                            Weather.main = weatherData.get("main").asString
                            Weather.description = weatherData.get("description").asString
                            weatherDataList.add(Weather.main)
                            weatherDataList.add(Weather.description)
                        }
                        completion(weatherDataList)
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "접속 실패, 태그: $t")
            }
        })
    }
}