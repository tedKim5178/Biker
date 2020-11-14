package com.mk.bikey.support.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mk.bikey.model.BikerLatLng
import com.mk.bikey.support.fromJson

class Converters {
    @TypeConverter
    fun fromBikerLatLng(latlngs: List<BikerLatLng>): String {
        return Gson().toJson(latlngs)
    }

    @TypeConverter
    fun fromString(value: String): List<BikerLatLng> {
        return Gson().fromJson<List<BikerLatLng>>(value)
    }
}