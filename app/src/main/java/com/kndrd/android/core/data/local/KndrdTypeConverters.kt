package com.kndrd.android.core.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class KndrdTypeConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> =
        gson.fromJson(value, object : TypeToken<List<String>>() {}.type) ?: emptyList()
}
