package com.mk.bikey.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "record")
@Parcelize
data class Record(
    @PrimaryKey
    val timestamp: Long,
    /**
     * in meters/second over ground
     */
    val avgSpeed: Float,
    /**
     * in meters/second over ground
     */
    val maxSpeed: Float,
    /**
     * meter
     */
    val distance: Float,
    val time: Long,
    /**
     * minutes
     */
    val latLngList: List<BikerLatLng>,
    val title: String = ""
) : Parcelable
