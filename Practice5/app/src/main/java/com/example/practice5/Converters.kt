package com.example.practice5

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split(",") ?: emptyList()
    }

    @TypeConverter
    fun fromDimensions(dimensions: Dimensions?): String? {
        return dimensions?.let { "${it.width},${it.height},${it.depth}" }
    }

    @TypeConverter
    fun toDimensions(value: String?): Dimensions? {
        return value?.split(",")?.let {
            if (it.size == 3) {
                Dimensions(it[0].toFloat(), it[1].toFloat(), it[2].toFloat())
            } else {
                null
            }
        }
    }

    @TypeConverter
    fun fromReviewList(reviews: List<Review>?): String? {
        return gson.toJson(reviews)
    }

    @TypeConverter
    fun toReviewList(value: String?): List<Review>? {
        val listType = object : TypeToken<List<Review>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromMeta(meta: Meta?): String? {
        return gson.toJson(meta)
    }

    @TypeConverter
    fun toMeta(value: String?): Meta? {
        return gson.fromJson(value, Meta::class.java)
    }
}
