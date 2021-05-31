package com.example.weatherwithgps_sample

import android.app.Application

class App : Application() {
    // 싱글턴으로 사용
    companion object{
        lateinit var context : App
        var provider = ""
        var countryName = ""
        var stateName = ""
        var cityName = ""
    }

    // 항상 어떤 Activity가 실행되면 해당 Activity의 context 가져오게 하기
    override fun onCreate() {
        super.onCreate()
        context = this
    }
}