package ru.netology.nmedia.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ktx.awaitAnimateCamera
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.model.cameraPosition
import com.google.maps.android.ktx.utils.collection.addMarker
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import ru.netology.nmedia.R
import ru.netology.nmedia.ui.dto.Marker
import ru.netology.nmedia.ui.util.CoordinatesArg
import ru.netology.nmedia.ui.util.EditedArg
import ru.netology.nmedia.ui.util.MarkerArg
import ru.netology.nmedia.ui.util.StringArg
import ru.netology.nmedia.ui.viewmodel.MarkerViewModel

class MapsFragment : Fragment() {
    private lateinit var googleMap: GoogleMap

    companion object {
        var Bundle.coordinatesData: DoubleArray? by CoordinatesArg}

    private val viewModel: MarkerViewModel by viewModels(
        ownerProducer = ::requireParentFragment)

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                googleMap.apply {
                    isMyLocationEnabled = true
                    uiSettings.isMyLocationButtonEnabled = true
                    mapType = GoogleMap.MAP_TYPE_NORMAL
                }
            } else {
                val dialogBuilder = AlertDialog.Builder(requireActivity())
                dialogBuilder.setMessage(R.string.sorry_dialog)
                    .setCancelable(false)
                    .setPositiveButton(
                        R.string.ok
                    ) { dialog, id ->
                        dialog.dismiss()
                    }
                val alert = dialogBuilder.create()
                alert.setTitle(R.string.lack_of_permission)
                alert.show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fabListMarkers = view.findViewById<View>(R.id.fabListMarkers)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        lifecycle.coroutineScope.launchWhenCreated {
            googleMap = mapFragment.awaitMap().apply {
                isTrafficEnabled = true
                isBuildingsEnabled = true

                uiSettings.apply {
                    isZoomControlsEnabled = true
                    isCompassEnabled = true
                    isIndoorLevelPickerEnabled = true
                    isMapToolbarEnabled = true
                    setAllGesturesEnabled(true)
                }
            }

            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    googleMap.apply {
                        isMyLocationEnabled = true
                        uiSettings.isMyLocationButtonEnabled = true
                    }

                    val fusedLocationProviderClient = LocationServices
                        .getFusedLocationProviderClient(requireActivity())

                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { println(it) }
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    val dialogBuilder = AlertDialog.Builder(requireActivity())
                    dialogBuilder.setMessage(R.string.access_dialog)
                        .setCancelable(false)
                        .setPositiveButton(
                            R.string.ok
                        ) { dialog, _ ->
                            dialog.dismiss()
                        }
                    val alert = dialogBuilder.create()
                    alert.setTitle(R.string.need_for_permission)
                    alert.show()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }

            val target = LatLng(55.751999, 37.617734)
            val userTarget = viewModel.places.observe(viewLifecycleOwner, Observer { marker })
//            val userTarget = arguments?.coordinatesData?.let { LatLng(it.first(), it.last()) }
            val markerManager = MarkerManager(googleMap)
            val collection: MarkerManager.Collection = markerManager.newCollection()

            collection.setOnMarkerClickListener { marker ->
                marker.showInfoWindow()
                true
            }

            googleMap.setOnMapClickListener { marker ->
                addMarkers(collection, marker, getString(R.string.new_marker_title))
                Toast.makeText(
                    requireContext(), R.string.add_marker, Toast.LENGTH_SHORT
                ).show()
                viewModel.changeData(
                    title = getString(R.string.new_marker_title),
                    latitude = marker.latitude,
                    longitude = marker.longitude
                )
                viewModel.save()
            }

            fabListMarkers.setOnClickListener {
                findNavController().navigate(
                    R.id.action_mapsFragment2_to_markersListFragment
                )
            }
            viewModel.places.observe(viewLifecycleOwner) {
                googleMap.awaitAnimateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        cameraPosition {
                            if (userTarget != null) {
                                target(userTarget)
                            } else {
                                target(target)
                            }
                            zoom(15F)
                        }
                    )
                )
            }

            viewModel.data.collect { data ->
                try {
                    collection.clear()
                    data.forEach { marker ->
                        addMarkers(
                            collection,
                            LatLng(marker.latitude, marker.longitude),
                            marker.title
                        )
                        collection.markers.map {
                            if (it.tag == null) it.tag = marker
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

fun addMarkers(collection: MarkerManager.Collection, latLng: LatLng, title: String) {
    collection.addMarker {
        position(latLng)
        title(title)
        snippet("$latLng")
    }
}