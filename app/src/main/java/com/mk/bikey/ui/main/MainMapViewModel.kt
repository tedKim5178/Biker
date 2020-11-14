
package com.mk.bikey.ui.main

import android.location.Location
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mk.bikey.database.BikerAppDatabase
import com.mk.bikey.database.RecordDao
import com.mk.bikey.model.BikerLatLng
import com.mk.bikey.model.Record
import com.mk.bikey.model.Route
import com.mk.bikey.repository.RecordRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainMapViewModel @ViewModelInject constructor(
    val recordRepository: RecordRepository,
    val recordDao: RecordDao
) : ViewModel() {

    private val reference by lazy { Firebase.database.reference }
    private val analytics by lazy { Firebase.analytics }

    private val _routeData = MutableLiveData<List<BikerLatLng>>()
    val routeData: LiveData<List<BikerLatLng>> = _routeData

    private var lastLocation = MutableLiveData<Location>()

    val speed: LiveData<Int> = Transformations.map(lastLocation) { location ->
        location?.speed?.toInt() ?: 0
    }

    private val _record = MutableLiveData<Record>()
    val record: LiveData<Record> = _record
    private val _recordStart = MutableLiveData<Boolean>()
    val recordStart: LiveData<Boolean> = _recordStart

    fun createRoute() {
        Observable.interval(5000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    lastLocation.value?.let {
                        val routeData = _routeData.value?.toMutableList() ?: return@let
                        routeData.add(
                            BikerLatLng(
                                it.latitude,
                                it.longitude
                            )
                        )
                        _routeData.value = routeData
                    }
                },
                onError = {
                    Timber.e(it)
                }
            )
    }

    fun recordRoute() {
        startLocation = lastLocation.value
        Observable.interval(0, 3000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    _recordStart.value = true
                    locationCount++
                    lastLocation.value?.let {
                        val speed = it.speed
                        sumOfSpeed += speed
                        if (speed > maxSpeed) {
                            maxSpeed = speed
                        }
                        val routeData = _routeData.value?.toMutableList() ?: mutableListOf()
                        routeData.add(
                            BikerLatLng(
                                it.latitude,
                                it.longitude
                            )
                        )
                        _routeData.value = routeData

                    }
                    val arrivalLocation = lastLocation.value
                    val departureLocation = startLocation
                    if (departureLocation != null && arrivalLocation != null) {
                        val timestamp = System.currentTimeMillis()
                        val avgSpeed = sumOfSpeed / locationCount
                        val maxSpeed = maxSpeed
                        val distance = arrivalLocation.distanceTo(startLocation)
                        val time = arrivalLocation.time - departureLocation.time
                        _record.value = Record(
                            timestamp = timestamp,
                            avgSpeed = avgSpeed,
                            maxSpeed = maxSpeed,
                            distance = distance,
                            latLngList = _routeData.value?.toList() ?: return@subscribeBy,
                            time = (time / 1000 / 60)
                        )
                        Timber.d("route = ${_record.value}")
                    }
                },
                onError = {
                    Timber.e(it)
                }
            )
    }

    private var sumOfSpeed = 0f
    private var locationCount = 0
    private var startLocation: Location? = null
    private var maxSpeed = 0f

    fun saveRouteToDatabase(title: String) {
        _recordStart.value = false
        val record = _record.value?.copy(title = title)
        record?.let {
            GlobalScope.launch {
                recordDao.insertRecord(it)
            }
        }
    }

    fun onLocationChange(location: Location) {
        lastLocation.value = location
    }

    fun updateRouteToFirebase(name: String) {
        val key = reference.child("route").push().key
        key?.let {
            reference.child("route").child(it).setValue(
                Route(name, routeData.value?.toList() ?: return@let)
            )
        }
    }

    fun sendResumeEvent(screenName: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }

    fun sendRouteCreateEvent(routeName: String) {
        analytics.logEvent("course_created") {
            param("name", routeName)
        }
    }
}