package com.projects.sydnyrestaurant.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class Converters {
    private val gson = Gson()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    @TypeConverter
    fun fromArrayList(dateList: ArrayList<Date>?): String {
        return gson.toJson(dateList?.map { dateFormat.format(it) })
    }

    @TypeConverter
    fun toArrayList(data: String?): ArrayList<Date> {
        if (data.isNullOrEmpty()) return arrayListOf()
        val type = object : TypeToken<ArrayList<String>>() {}.type
        val dateStrings: ArrayList<String> = gson.fromJson(data, type)
        return dateStrings.map { dateFormat.parse(it)!! } as ArrayList<Date>
    }
}
