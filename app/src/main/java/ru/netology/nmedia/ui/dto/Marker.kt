package ru.netology.nmedia.ui.dto

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class Marker(
    val id: Long,
    val coordinates: LatLng?,
    val name: String,
) : Parcelable
