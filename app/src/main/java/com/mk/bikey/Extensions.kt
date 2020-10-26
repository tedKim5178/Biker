package com.mk.bikey

import android.location.Location

/**
 * Location 을 customizing 함으로써 library 의 의존도를 낮춤
 */
fun Location.toBikerLocation(): BikerLocation
        = BikerLocation(
    latitude = latitude,
    longitude = longitude,
    speed = speed
)