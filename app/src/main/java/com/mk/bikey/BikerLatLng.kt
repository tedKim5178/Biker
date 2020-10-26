package com.mk.bikey

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class BikerLatLng(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : Parcelable