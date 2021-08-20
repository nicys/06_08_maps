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
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ktx.awaitAnimateCamera
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.model.cameraPosition
import com.google.maps.android.ktx.utils.collection.addMarker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.nmedia.R
import ru.netology.nmedia.ui.EditNameFragment.Companion.textArg
import ru.netology.nmedia.ui.dto.Marker
import ru.netology.nmedia.ui.viewmodel.MarkerViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class MapsFragment : Fragment() {
    private lateinit var googleMap: GoogleMap

    private var markers = emptyList<Marker>()

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
                    // if the dialog is cancelable
                    .setCancelable(false)
                    .setPositiveButton(
                        R.string.ok,
                        DialogInterface.OnClickListener { dialog, id ->
                            dialog.dismiss()
                        })
                val alert = dialogBuilder.create()
                alert.setTitle(R.string.lack_of_permission)
                alert.show()
//                 // TODO: show sorry dialog
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    val viewModel: MarkerViewModel by viewModels()

    @ExperimentalCoroutinesApi
    @SuppressLint("PotentialBehaviorOverride", "ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        val editMarkerText = childFragmentManager.findFragmentById(R.id.editNameMarker) as SupportMapFragment


        viewModel.data.observe(viewLifecycleOwner, {
        })

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

                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { println(it) }
                }
                // 2. Должны показать обоснование необходимости прав
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    val dialogBuilder = AlertDialog.Builder(requireActivity())
                    dialogBuilder.setMessage(R.string.access_dialog)
                        // if the dialog is cancelable
                        .setCancelable(false)
                        .setPositiveButton(
                            R.string.ok,
                            DialogInterface.OnClickListener { dialog, id ->
                                dialog.dismiss()
                            })
                    val alert = dialogBuilder.create()
                    alert.setTitle(R.string.need_for_permission)
                    alert.show()
//                      // TODO: show rationale dialog
                }
                // 3. Запрашиваем права
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }

            val markerManager = MarkerManager(googleMap)
            val collection: MarkerManager.Collection = markerManager.newCollection()

            mapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
                googleMap.setOnMapClickListener {
                    val marker = collection.addMarker {
                        position(it)
                        title("")
                        snippet("$position")
                    }.apply {
                        showInfoWindow()
                        tag = dateText
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


//            val collection: MarkerManager.Collection = markerManager.newCollection()
////                .apply {
////                addMarker {
////                    position
////                    title
////                    snippet
////                }
////            }
//            mapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
//                googleMap.setOnMapClickListener {
//                    val marker = collection.addMarker {
//                        position(it)
//                        title("")
////                    snippet("$position")
//                    }
//                    findNavController().navigate(R.id.action_mapsFragment_to_editNameFragment,
//                        Bundle().apply
//                        { textArg = marker.title })
//                }
//            })


//            mapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
//                googleMap.setOnMapClickListener { point ->
//                    val collection: MarkerManager.Collection = markerManager.newCollection().apply {
//                        addMarker {
//                            position(point)
//                            title("$position")
////                            snippet("----")
////                            add(title, point)
//                        }.apply {
//                            showInfoWindow()
//                            tag = dateText
//                            Toast.makeText(requireContext(), R.string.add_marker, Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                        with(googleMap) {
//                            animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12f))
//                            cameraPosition
//                        }
//                    }
//
//                    collection.setOnMarkerClickListener { marker ->
//
//                        val dialogBuilder = AlertDialog.Builder(requireActivity())
//                        dialogBuilder.setTitle(R.string.change_marker)
//                        dialogBuilder.setMessage(R.string.do_marker)
//                        dialogBuilder.setPositiveButton(R.string.remove) { dialog, which ->
//                            marker.remove()
//                            Toast.makeText(requireContext(), R.string.remove_marker, Toast.LENGTH_SHORT
//                            ).show()
//                        }
//
//                        dialogBuilder.setNegativeButton(R.string.change) { dialog, which ->
//
//                            val text = getText(R.id.editNameMarker)
//
//                            marker.snippet = text.toString()
//                            marker.showInfoWindow()
//                        }
//
//                        dialogBuilder.setNeutralButton(R.string.cancel) { dialog, _ ->
//                            dialog.dismiss()
//                        }
//
//                        val dialog: AlertDialog = dialogBuilder.create()
//                        dialog.show()
//                        true
//                    }
//                }
//            })
//
////            googleMap.awaitAnimateCamera(
////                    CameraUpdateFactory.newCameraPosition(
////                            cameraPosition {
////                                target(target)
////                                zoom(15F)
////                            }
////                    ))
        }
    }

    private val _markers: MutableMap<String, MarkerOptions> =
        ConcurrentHashMap<String, MarkerOptions>()

    private fun add(name: String, latLong: LatLng) {
        val marker = MarkerOptions().position(latLong).title(name)
        _markers[name] = marker
    }

    private fun remove(name: String): Boolean {
        _markers.remove(name)
        googleMap.clear()
        for (item in _markers.values) {
            googleMap.addMarker(item)
        }
        return true
    }

    var currentDate: Date = Date()
    var dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    var dateText: String = dateFormat.format(currentDate)

    var timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    var timeText: String = timeFormat.format(currentDate)
}


//internal class InfoWindowActivity : AppCompatActivity(),
//        GoogleMap.OnInfoWindowClickListener,
//        OnMapReadyCallback {
//    override fun onMapReady(googleMap: GoogleMap) {
//        googleMap.setOnMapLongClickListener {
//
//        }
//    }
//
//    override fun onInfoWindowClick(p0: com.google.android.gms.maps.model.Marker) {
//        Toast.makeText(
//                this, "Info window clicked",
//                Toast.LENGTH_SHORT
//        ).show()
//    }
//}


//            mapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
//                googleMap.setOnMapClickListener { point ->
//                    val marker = MarkerOptions().apply {
//                        position(point)
//                        isDraggable
//                        infoWindowAnchorU
//                        title("Ш-${point.latitude} : Д-${point.longitude}")
//                        add(title, point)
//                    }
//                    with(googleMap) {
//                        addMarker(marker)
//                        animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12f))
//                        cameraPosition
//                    }
//                }
//            })

//            mapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
//                googleMap.setOnMarkerClickListener {
//                    !isVisible
//                }
//            })
