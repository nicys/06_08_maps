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
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ktx.awaitAnimateCamera
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.model.cameraPosition
import com.google.maps.android.ktx.utils.collection.addMarker
import ru.netology.nmedia.R
import ru.netology.nmedia.ui.extensions.icon
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class MapsFragment : Fragment() {
    private lateinit var googleMap: GoogleMap

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
                        R.string.ok,
                        DialogInterface.OnClickListener { dialog, id ->
                            dialog.dismiss()
                        })
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

            val target = LatLng(55.751999, 37.617734)
            val markerManager = MarkerManager(googleMap)
            val collection: MarkerManager.Collection = markerManager.newCollection().apply {
                addMarker {
                    position(target)
                    icon(getDrawable(requireContext(), R.drawable.ic_netology_48dp)!!)
                    title("The Moscow Kremlin")
                }.apply {
                    tag = "Any additional data" // Any
                }
            }
            collection.setOnMarkerClickListener { marker ->
                // TODO: work with marker
                Toast.makeText(
                    requireContext(),
                    (marker.tag as String),
                    Toast.LENGTH_LONG
                ).show()
                true
            }

            googleMap.awaitAnimateCamera(
                CameraUpdateFactory.newCameraPosition(
                    cameraPosition {
                        target(target)
                        zoom(15F)
                    }
                ))
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



