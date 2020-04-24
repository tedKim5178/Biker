package com.example.bikey

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource

class MainActivity : AppCompatActivity(), OnMapReadyCallback, Handler.Callback{

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val UPDATE_PATH_INTERVAL_MS = 5_000L
        private const val TAG = "[MK]"
    }
    private lateinit var speedTv: TextView
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var path: PathOverlay

    /**
     * 위치 정보 리스트
     */
    private val coords = mutableListOf<LatLng>()
    /**
     * 마지막으로 받은 위치 정보
     */
    private var lastLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        speedTv = findViewById(R.id.speed)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
        mapFragment?.getMapAsync(this)
        // 내 location 위치를 네이버 맵에 제공해줄 객체
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        setPathOverlay()
        startPathUpdater()
    }

    private fun setPathOverlay() {
        path = PathOverlay()
        path.width = 15
        path.color = Color.RED
    }

    private fun startPathUpdater() {
        Handler().postDelayed({ updatePaths() }, UPDATE_PATH_INTERVAL_MS)
    }

    private fun updatePaths() {
        lastLocation?.let {
            val latitude = it.latitude
            val longitude = it.longitude
            Log.d(TAG, "latitude = $latitude, longitude = $longitude")
            coords.add(LatLng(latitude, longitude))
        }
        if (coords.size < 2) return
        path.coords = coords
        path.map = naverMap
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        // 이제 맵에서 내 단말 로케이션 정보를 지원해줄 수 있음
        naverMap.locationSource = locationSource
        // 내 위치 파란점으로 보임
        naverMap.locationOverlay.isVisible = true
        // 내 위치 팔로우 (카메라까지)
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        naverMap.addOnLocationChangeListener {
            lastLocation = it
            speedTv.text = it.speed.toString()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun handleMessage(msg: Message): Boolean {
        msg.callback.run()
        return false
    }
}
