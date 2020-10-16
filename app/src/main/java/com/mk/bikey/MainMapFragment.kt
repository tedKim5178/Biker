package com.mk.bikey

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mk.bikey.databinding.FragmentMainMapBinding
import com.mk.bikey.databinding.NavHeaderBinding
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMainMapBinding
    private lateinit var navBinding: NavHeaderBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val path: PathOverlay = PathOverlay()
    private var lastLatLng: LatLng? = null
    private val latLngList: MutableList<LatLng> = mutableListOf()
    private var disposable: Disposable? = null
    private var route: Route? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentMainMapBinding.inflate(inflater, container, false).run {
        binding = this
        // TODO(fix me)
        navBinding = NavHeaderBinding.bind(binding.navView.getHeaderView(0))
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        initLocationSource()
        setBinding()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Route?>("route")
            ?.observe(viewLifecycleOwner, Observer {
                Timber.d("loaded route = $it")
                route = it
            })
    }

    private fun setBinding() {
        with(binding) {
            imgMenu.setOnClickListener {
                containerDrawer.open()
            }
            btnStart.setOnClickListener {
                it.visibility = View.GONE
                btnStop.visibility = View.VISIBLE
                startRouteMaking()
            }
            btnStop.setOnClickListener {
                disposable?.dispose()
                it.visibility = View.GONE
                InputDialog.show(
                    fragmentManager = childFragmentManager,
                    titleText = getString(R.string.input_course_dialog_title),
                    onConfirm = { inputText ->
                        updateLatLngListToServer(inputText)
                    }
                )
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
        }
    }

    private fun onRouteCreateClicked() {
        binding.btnStart.visibility = View.VISIBLE
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        getMapReady()
    }

    private fun updateLatLngListToServer(name: String) {
        val route = Route(name, latLngList)
        val database = Firebase.database
        val reference = database.reference
        val key = reference.child("route").push().key
        key?.let {
            reference.child("route").child(it).setValue(route)
        }
    }

    private fun startRouteMaking() {
        disposable = Observable.interval(5000, TimeUnit.MILLISECONDS)
            .subscribeBy(
                onNext = { lastLatLng?.let { latLngList.add(it) } },
                onError = { Timber.e(it) }
            )
    }

    private fun getMapReady() {
        with(naverMap) {
            locationSource = this@MainMapFragment.locationSource
            locationOverlay.isVisible = true
            locationTrackingMode = LocationTrackingMode.Follow
            addOnLocationChangeListener {
                lastLatLng = LatLng(it.latitude, it.longitude)
                binding.speed.text = (it.speed * 3.6f).toInt().toString()
            }

            try {
                route?.let {
                    clear()
                    path.coords = it.latlngList.map { temp ->
                        com.naver.maps.geometry.LatLng(
                            temp.latitude,
                            temp.longitude
                        )
                    }
                    path.width = 10
                    path.outlineWidth = 10
                    path.color = Color.BLUE
                    path.map = this
                }
            } catch (e: Exception) {
                Timber.e(e)
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