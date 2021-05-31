package com.example.weatherwithgps_sample.retrofit

import android.util.Log
import com.example.weatherwithgps_sample.API
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
    fun getWeatherData(cityName : String,
                       stateCode : String,
                       countryCode : String,
                       appId : String){
        val call = iRetrofit?.getWeatherData(cityName, stateCode, countryCode, appId) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit", "접속 성공, response: ${response.body()}, code: ${response.code()}")
                response.body()?.let {

                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "접속 실패, 태그: $t")
            }
        })
    }
}