package com.mk.bikey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mk.bikey.databinding.FragmentMainMapBinding
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMainMapBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var path: PathOverlay

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentMainMapBinding.inflate(inflater, container, false).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        initLocationSource()
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        getMapReady()
    }

    private fun getMapReady() {
        with(naverMap) {
            locationSource = this@MainMapFragment.locationSource
            locationOverlay.isVisible = true
            locationTrackingMode = LocationTrackingMode.Follow
            addOnLocationChangeListener { currentLocation ->
                binding.speed.text = (currentLocation.speed * 3.6f).toInt().toString()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clear()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment?.getMapAsync(this)
    }

    private fun initLocationSource() {
        locationSource = FusedLocationSource(this, MainActivity.LOCATION_PERMISSION_REQUEST_CODE)
    }


    private fun clear() {
        path.map = null
    }
}