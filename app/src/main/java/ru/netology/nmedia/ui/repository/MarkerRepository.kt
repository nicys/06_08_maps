package ru.netology.nmedia.ui.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng
import ru.netology.nmedia.ui.dto.Marker

interface MarkerRepository {
    fun getAll(): LiveData<List<Marker>>
    fun addMarker(marker: Marker)
    fun removeMarker(coord: LatLng)
    fun snippetByCoord(coord: LatLng)
}