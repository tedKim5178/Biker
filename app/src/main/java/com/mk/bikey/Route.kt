package com.mk.bikey

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Route(
    val name: String = "",
    val latlngList: List<LatLng> = mutableListOf()
) : Parcelable