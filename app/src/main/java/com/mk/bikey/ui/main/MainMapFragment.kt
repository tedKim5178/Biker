package com.mk.bikey.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.mk.bikey.*
import com.mk.bikey.databinding.FragmentMainMapBinding
import com.mk.bikey.databinding.NavHeaderBinding
import com.mk.bikey.model.BikerLocation
import com.mk.bikey.model.Route
import com.mk.bikey.ui.dialog.InputDialog
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMainMapBinding
    private lateinit var navBinding: NavHeaderBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val path: PathOverlay = PathOverlay()
    private var route: Route? = null
    private val mainMapViewModel: MainMapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentMainMapBinding.inflate(inflater, container, false).run {
        binding = this
        navBinding = NavHeaderBinding.bind(binding.navView.getHeaderView(0))
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        initLocationSource()
        setBinding()
        observeUi()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Route?>("route")
            ?.observe(viewLifecycleOwner, Observer {
                Timber.d("loaded route = $it")
                route = it
            })
    }

    override fun onResume() {
        super.onResume()
        mainMapViewModel.sendResumeEvent("MainMapFragment")
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment?.getMapAsync(this)
    }

    private fun initLocationSource() {
        locationSource = FusedLocationSource(this,
            MainActivity.LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun setBinding() {
        with(binding) {
            imgMenu.setOnClickListener {
                containerDrawer.open()
            }
            btnStart.setOnClickListener {
                it.visibility = View.GONE
                btnStop.visibility = View.VISIBLE
                createRoute()
            }
            btnStop.setOnClickListener {
                it.visibility = View.GONE
                showNameRouteDialog()
            }
        }
        with(navBinding) {
            tvLoad.setOnClickListener {
                binding.containerDrawer.close()
                it.findNavController().navigate(R.id.action_mainMapFragment_to_searchRouteFragment)
            }
            tvCreate.setOnClickListener {
                binding.containerDrawer.close()
                onRouteCreateClicked()
            }
            tvRecord.setOnClickListener {
                TODO("not implemented")
            }
            tvHistory.setOnClickListener {
                TODO("not implemented")
            }
        }
    }

    private fun showNameRouteDialog() {
        InputDialog.show(
            fragmentManager = childFragmentManager,
            titleText = getString(R.string.input_course_dialog_title),
            onConfirm = { routeName ->
                mainMapViewModel.sendRouteCreateEvent(routeName)
                updateLatLngListToServer(routeName)
            }
        )
    }

    private fun observeUi() {
        observeSpeedKph()
    }

    private fun observeSpeedKph() {
        mainMapViewModel.speedKph.observe(viewLifecycleOwner, Observer {
            binding.speed.text = it.toString()
        })
    }

    private fun onRouteCreateClicked() {
        binding.btnStart.visibility = View.VISIBLE
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        getMapReady()
    }

    private fun updateLatLngListToServer(name: String) {
        mainMapViewModel.updateRouteToFirebase(name)
    }

    private fun createRoute() {
        mainMapViewModel.createRoute()
    }

    private fun getMapReady() {
        with(naverMap) {
            locationSource = this@MainMapFragment.locationSource
            locationOverlay.isVisible = true
            locationTrackingMode = LocationTrackingMode.Follow
            addOnLocationChangeListener {
                mainMapViewModel.onLocationChange(
                    BikerLocation(
                        it.latitude,
                        it.longitude,
                        it.speed
                    )
                )
            }
            loadRoutePathIfExist()
        }
    }

    private fun loadRoutePathIfExist() {
        try {
            route?.let {
                clear()
                with(path) {
                    coords = it.latlngList.map { LatLng(it.latitude, it.longitude) }
                    width = 10
                    outlineWidth = 10
                    color = Color.BLUE
                    map = naverMap
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        clear()
    }

    private fun clear() {
        path.map = null
    }
}