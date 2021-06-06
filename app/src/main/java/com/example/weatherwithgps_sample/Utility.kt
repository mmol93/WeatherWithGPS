package com.example.weatherwithgps_sample

import java.util.Calendar
import kotlin.collections.ArrayList

// 오늘부터 일주일치의 요일을 가져오는 기능
object Calendar {
    val calendar = Calendar.getInstance()
    val weekList = ArrayList<String>()

    fun getWeek() : ArrayList<String> {
        for (i in 1..7){
            val week_cal = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val week = calendar.get(Calendar.DAY_OF_WEEK)
            when(week){
                1 -> weekList.add(App.context.getString(R.string.week_sun))
                2 -> weekList.add(App.context.getString(R.string.week_mon))
                3 -> weekList.add(App.context.getString(R.string.week_tue))
                4 -> weekList.add(App.context.getString(R.string.week_wed))
                5 -> weekList.add(App.context.getString(R.string.week_thur))
                6 -> weekList.add(App.context.getString(R.string.week_fri))
                7 -> weekList.add(App.context.getString(R.string.week_sat))
            }
        }
        return weekList
    }
}