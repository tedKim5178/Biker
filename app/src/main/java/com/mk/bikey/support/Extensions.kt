package com.mk.bikey.support

import android.location.Location
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mk.bikey.model.BikerLocation

/**
 * Location 을 customizing 함으로써 library 의 의존도를 낮춤
 */
fun Location.toBikerLocation(): BikerLocation = BikerLocation(
    latitude = latitude,
    longitude = longitude,
    speed = speed
)

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)