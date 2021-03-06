package ru.netology.nmedia.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.utils.collection.addMarker
import ru.netology.nmedia.R
import ru.netology.nmedia.ui.viewmodel.MarkerViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap


class MapsFragment : Fragment() {

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
        Log.d("logzzz", "onCV")

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    @SuppressLint("ResourceType")
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
                // 1. ?????????????????? ???????? ???? ?????? ??????????
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
                // 2. ???????????? ???????????????? ?????????????????????? ?????????????????????????? ????????
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
                }
                // 3. ?????????????????????? ??????????
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }

            val markerManager = MarkerManager(googleMap)
            mapFragment.getMapAsync(OnMapReadyCallback {
                it.setOnMapClickListener { point ->
                    val collection: MarkerManager.Collection = markerManager.newCollection().apply {
                        addMarker {
                            position(point)
                            title("??-${point.latitude}")
                            snippet("-")
                            add(title!!, point)
                            viewModel.addMarker()
                        }.apply {
                            showInfoWindow()
                            tag = dateText
                            Toast.makeText(
                                requireContext(), R.string.add_marker, Toast.LENGTH_SHORT
                            ).show()
                        }
                        with(googleMap) {
                            animateCamera(CameraUpdateFactory.newLatLngZoom(point, 15f))
                            cameraPosition
                        }
                    }

                    collection.setOnMarkerClickListener { marker ->
                        val dialogBuilder = AlertDialog.Builder(requireActivity())
                        dialogBuilder.setTitle(R.string.change_marker)
                        dialogBuilder.setMessage(R.string.do_marker)
                        dialogBuilder.setPositiveButton(R.string.REMOVE) { dialog, which ->
                            marker.remove()
                            viewModel.removeMarker(point)
                            Toast.makeText(
                                requireContext(), R.string.remove_marker, Toast.LENGTH_SHORT
                            ).show()
                        }

                        dialogBuilder.setNegativeButton(R.string.CHANGER) { dialog, which ->


//                            val bundle: Bundle? = arguments
//                            val newSnippet = bundle?.getString("message")


//                            val intent = arguments
//                            val newSnippet = if (savedInstanceState != null) {
//                                val extras = intent?.getBundle("message")
//                                extras?.getString("message")
//                            } else {
//                                savedInstanceState?.getString("message", "555555") as String?
//                            }
//                            marker.snippet = newSnippet.toString()

//                            marker.snippet = newSnippet

                            marker.showInfoWindow()
                        }

                        dialogBuilder.setNeutralButton(R.string.CANCEL) { dialog, _ ->
                            marker.showInfoWindow()
//                            dialog.dismiss()
                        }

                        val dialog: AlertDialog = dialogBuilder.create()
                        dialog.show()
                        true
                    }
                }
            })

//            googleMap.awaitAnimateCamera(
//                CameraUpdateFactory.newCameraPosition(
//                    cameraPosition {
//                        target(target)
//                        zoom(15F)
//                    }
//                ))
        }


    }


    private val _markers: MutableMap<String, MarkerOptions> =
        ConcurrentHashMap<String, MarkerOptions>()

    private fun add(name: String, latLong: LatLng) {
        val marker = MarkerOptions().position(latLong).title(name)
        _markers[name] = marker
    }

//    private fun remove(latLong: LatLng): Boolean {
//        _markers.remove(latLong)
//        googleMap.clear()
//        for (item in _markers.values) {
//            googleMap.addMarker(item)
//        }
//        return true
//    }

    var currentDate: Date = Date()
    var dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    var dateText: String = dateFormat.format(currentDate)

    var timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    var timeText: String = timeFormat.format(currentDate)

    override fun onStart() {
        super.onStart()
        Log.d("logzzz", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("logzzz", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("logzzz", "onStop")
    }
}




