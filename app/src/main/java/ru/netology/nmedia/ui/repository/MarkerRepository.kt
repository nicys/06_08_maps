package ru.netology.nmedia.ui.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng
import ru.netology.nmedia.ui.dto.MyMarker

interface MarkerRepository {
    fun getAll(): LiveData<List<MyMarker>>
    fun addMarker(myMarker: MyMarker)
    fun removeMarker(coord: LatLng)
}