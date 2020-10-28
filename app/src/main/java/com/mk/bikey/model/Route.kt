package com.mk.bikey.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.mk.bikey.model.BikerLatLng
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Route(
    val name: String = "",
    val latlngList: List<BikerLatLng> = mutableListOf()
) : Parcelable