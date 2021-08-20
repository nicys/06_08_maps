package ru.netology.nmedia.ui.dto

import com.google.android.gms.maps.model.LatLng

data class Place(
    val id: Short,
    val name: String,
    val location: LatLng,
)
