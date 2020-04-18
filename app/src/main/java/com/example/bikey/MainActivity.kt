package com.example.bikey

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity(), LocationListener{

    private lateinit var speedTv: TextView
    private var lastLocation: Location? = null
    private var calculatedSpeed = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        speedTv = findViewById(R.id.speed)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)

        } else {
            requestLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        val locationManager = (getSystemService(Context.LOCATION_SERVICE) as? LocationManager)
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1f, this)
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
    }

    override fun onLocationChanged(location: Location?) {
        if (location != null && lastLocation != null) {
            val time = (location.time - lastLocation!!.time) / 1_000f
            if (time < 0) return
            // TODO :: 0.1 자리 까지 바꾸기
            calculatedSpeed = (lastLocation!!.distanceTo(location) / time).toInt()
        }
        lastLocation = location
        val speed = if (location?.hasSpeed() == true) location.speed else calculatedSpeed
        speedTv.text = speed.toString()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do Stuff
                requestLocationUpdates()
            } else {
                finish()
            }
        }
    }
}
