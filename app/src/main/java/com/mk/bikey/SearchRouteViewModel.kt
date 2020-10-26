package com.mk.bikey

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SearchRouteViewModel @ViewModelInject constructor() : ViewModel() {

    private val reference by lazy { Firebase.database.reference }

    private val _routeList = MutableLiveData<List<Route>>()
    val routeList: LiveData<List<Route>> = _routeList

    fun fetch() {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                _routeList.value = snapshot.children.map {
                    it.getValue(Route::class.java) ?: return
                }
            }
        }
        reference.child("route").addListenerForSingleValueEvent(listener)
    }
}