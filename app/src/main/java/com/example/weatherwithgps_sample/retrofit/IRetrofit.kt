package com.example.weatherwithgps_sample.retrofit

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// baseUrl : https://api.openweathermap.org/
// url : api.openweathermap.org/data/2.5/weather?q={city name},{state code},{country code}&appid={API key}
interface IRetrofit {
    @GET("data/2.5/weather")
    fun getWeatherData(
        @Query("q") locationInfo : String,
        @Query("appid") Id : String) : Call<JsonElement>
}