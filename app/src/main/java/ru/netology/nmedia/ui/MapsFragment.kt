package ru.netology.nmedia.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ktx.awaitAnimateCamera
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.model.cameraPosition
import com.google.maps.android.ktx.utils.collection.addMarker
import kotlinx.coroutines.launch
import ru.netology.nmedia.R
import ru.netology.nmedia.ui.dto.Place
import ru.netology.nmedia.ui.extensions.icon
import ru.netology.nmedia.ui.viewmodel.MapViewModel

class MapsFragment : Fragment() {
    private lateinit var googleMap: GoogleMap

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                googleMap.apply {
                    isMyLocationEnabled = true
                    uiSettings.isMyLocationButtonEnabled = true
                }
            } else {
                // TODO: show sorry dialog
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        lifecycle.coroutineScope.launchWhenCreated {
            googleMap = mapFragment.awaitMap().apply {
                isTrafficEnabled = true
                isBuildingsEnabled = true

                uiSettings.apply {
                    isZoomControlsEnabled = true
                    setAllGesturesEnabled(true)
                }
            }

            when {
                // 1. Проверяем есть ли уже права
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

                    fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                        println(it)
                    }
                }
                // 2. Должны показать обоснование необходимости прав
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    // TODO: show rationale dialog
                }
                // 3. Запрашиваем права
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }

            val markerManager = MarkerManager(googleMap)
            val viewModel by viewModels<MapViewModel>(ownerProducer = ::requireActivity)
            val collection: MarkerManager.Collection = markerManager.newCollection()

            mapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
                googleMap.setOnMapClickListener {
                    val marker = collection.addMarker {
                        position(it)
                        title(" ")
                        snippet("$position")
                    }.apply {
                        showInfoWindow()

                        Toast.makeText(requireContext(), R.string.add_marker, Toast.LENGTH_SHORT
                        ).show()
                    }
                    with(googleMap) {
                        animateCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
                        cameraPosition
                    }
//                    findNavController().navigate(R.id.action_mapsFragment_to_editNameFragment,
//                        Bundle().apply
//                        { textArg = marker.title })
                }
            })

            viewModel.places.observe(viewLifecycleOwner) {
                it.forEach {
                    collection.addMarker {
                        position(it.location)
                        icon(getDrawable(requireContext(), R.drawable.ic_netology_48dp)!!)
                        title(it.name)
                    }.apply {
                        tag = it
                    }
                }
            }
            viewModel.selectedPlace.observe(viewLifecycleOwner) {
                lifecycleScope.launch {
                    googleMap.awaitAnimateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            cameraPosition {
                                target(it.location)
                                zoom(15F)
                            }
                        ))
                }
            }
            collection.setOnMarkerClickListener { marker ->
                viewModel.selectPlace(marker.tag as Place)
                true
            }
        }
    }
}