package com.example.weatherwithgps_sample.retrofit

import android.util.Log
import com.example.weatherwithgps_sample.API
import com.example.weatherwithgps_sample.Weather
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {
    companion object{
        val instance = RetrofitManager()
    }
    // 레트로핏 인터페이스 가져오기
    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.baseURL)?.create(IRetrofit::class.java)

    // 날씨 데이터를 가져오는 API 호출
    fun getCurrentWeatherData(locationInfo : String, AppId : String,
                       completion:() -> Unit){
        val call = iRetrofit?.getWeatherData(locationInfo, AppId) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit", "접속 성공\n, response: ${response.body()}, code: ${response.code()}")
                when(response.code()){
                    200 ->{
                        // response.body()에 어떤 값이 있을 경우
                        response.body().let {
                            // 1차: JsonObject 분해
                            val body = it!!.asJsonObject
                            // 2차: 원하는 JsonArray 가져와서 JsonObject로 분해
                            val weatherArray = body.getAsJsonArray("weather")
                            // 왜 0번 째 인덱스 참조하는지는 Json 데이터 참조하기
                            val weatherData = weatherArray[0].asJsonObject
                            // 3차 JsonObject 가져왔으면 그 안에서 필요한 요소 다시 가져오기
                            // 간단 날씨 설명
                            Weather.main = weatherData.get("main").asString
                            // 상세 날시 설명
                            Weather.description = weatherData.get("description").asString
                            // main 부분의 JsonObject 가져오기
                            val main = body.getAsJsonObject("main").asJsonObject
                            // 현재 날씨
                            Weather.temp = main.get("temp").asLong
                            // 체감온도
                            Weather.feels = main.get("feels_like").asLong
                            // 최대 온도
                            Weather.max_temp = main.get("temp_max").asLong
                            // 최저 온도
                            Weather.min_temp = main.get("temp_min").asLong
                            // 습도
                            Weather.humility = main.get("humidity").asLong

                            // 위의 온도는 전부 절대온도이기 때문에 섭씨로 변환해준다
                            Weather.temp = (Weather.temp - 273.15).toLong()
                            Weather.max_temp = (Weather.max_temp - 273.15).toLong()
                            Weather.min_temp = (Weather.min_temp - 273.15).toLong()
                            Weather.feels = (Weather.feels - 273.15).toLong()

                            // 바람에 관한 JsonObject 가져오기
                            val wind = body.getAsJsonObject("wind").asJsonObject
                            // 바람 속도
                            Weather.wind = wind.get("speed").asLong

                            // 시스템에 관한 값 가져오기
                            val sys = body.getAsJsonObject("sys").asJsonObject
                            // 해뜨는 시간
                            Weather.sunRise = sys.get("sunrise").asLong
                            // 해 지는 시간
                            Weather.sunSet = sys.get("sunset").asLong
                            // 국가
                            Weather.country = sys.get("country").asString

                        }
                        completion()
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "접속 실패, 태그: $t")
            }
        })
    }
    // 날씨 예보에 대한 API 가져오기
    fun getForecast(lat : String, lon : String, part : String, appid : String, completion: (
        ArrayList<Long>, ArrayList<Int>, ArrayList<Int>, ArrayList<Long>, ArrayList<String>
            ) -> Unit){
        val call = iRetrofit?.getForecast(lat, lon, part, appid) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit2", "접속 성공\n, response: ${response.body()}, code: ${response.code()}")
                when(response.code()){
                    200 -> {
                        response.body().let {
                            val body = it!!.asJsonObject
                            // hourly JsonArray 가져오기 - 여기에 시간별 날씨 데이터 있음
                            val hourlyForecast = body.getAsJsonArray("hourly")
                            // 각 데이터를 위한 리스트를 만든다
                            val hourlyTemp = ArrayList<Long>()  // 온도
                            val hourlyPop = ArrayList<Int>() // 강수 확률
                            val hourlyWind = ArrayList<Int>() // 바람 속도
                            val hourlyUvi = ArrayList<Long>() // 자외선 지수
                            val hourlyMain = ArrayList<String>() // 날씨 설명

                            val unit = 0..27 step 3    // for를 위한 카운터용 배열

                            // 인덱스는 0부터 하여 3시간 단위로 데이터를 가져온다
                            for (i in unit){
                                val forecastBody = hourlyForecast[i].asJsonObject
                                val temp = forecastBody.get("temp").asLong
                                val pop = forecastBody.get("pop").asInt
                                val wind = forecastBody.get("wind_speed").asInt
                                val uvi = forecastBody.get("uvi").asLong
                                val weatherArray = forecastBody.getAsJsonArray("weather")
                                val weatherBody = weatherArray[0].asJsonObject
                                val main = weatherBody.get("main").asString

                                Log.d("retrofit2", "temp: $temp")
                                Log.d("retrofit2", "pop: $pop")
                                Log.d("retrofit2", "wind: $wind")
                                Log.d("retrofit2", "uvi: $uvi")
                                Log.d("retrofit2", "main: $main")

                                // 온도의 경우 절대 온도로 가져오기 때문에 섭씨는 변환이 필요하다
                                val temp_trans = (temp - 273.15)

                                // 각 데이터를 리스트에 넣기
                                hourlyTemp.add(temp_trans.toLong())
                                hourlyPop.add(pop)
                                hourlyWind.add(wind)
                                hourlyUvi.add(uvi)
                                hourlyMain.add(main)
                            }
                            completion(hourlyTemp, hourlyPop, hourlyWind, hourlyUvi, hourlyMain)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit2", "접속 실패, 태그: $t")
            }
        })
    }
}

