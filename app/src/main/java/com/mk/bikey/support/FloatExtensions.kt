package com.mk.bikey.support

fun Float.mpsToKph(): Int  = (this * 3.6).toInt()
fun Float.mToKm(): Int = (this / 1000).toInt()