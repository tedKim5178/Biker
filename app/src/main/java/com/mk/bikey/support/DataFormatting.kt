package com.mk.bikey.support

import java.text.SimpleDateFormat
import java.util.*

object DateFormatting {
    @JvmStatic
    fun toAHMm(millis: Long): String =
        SimpleDateFormat("a h:mm", Locale.getDefault()).format(Date(millis))

    @JvmStatic
    fun toYyyyMD(millis: Long): String =
        SimpleDateFormat("yyyy년 M월 d일", Locale.getDefault()).format(Date(millis))

    @JvmStatic
    fun toYyyyMDH(millis: Long): String =
        SimpleDateFormat("yyyy년 M월 d일 h시", Locale.getDefault()).format(Date(millis))

    @JvmStatic
    fun toYyyyMmDdHhMm(millis: Long): String =
        SimpleDateFormat("yyyy년MM월dd일 hh:mm", Locale.getDefault()).format(Date(millis))

    @JvmStatic
    fun toYyMmDdHhMm(millis: Long): String =
        SimpleDateFormat("yy.MM.dd hh:mm", Locale.getDefault()).format(Date(millis))

    @JvmStatic
    fun toHhMm(millis: Long): String =
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(millis))

    @JvmStatic
    fun toMD(millis: Long): String =
        SimpleDateFormat("M월 d일", Locale.getDefault()).format(Date(millis))
}
