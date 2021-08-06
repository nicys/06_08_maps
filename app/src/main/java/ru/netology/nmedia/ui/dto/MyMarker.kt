package ru.netology.nmedia.ui.dto

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize


@Parcelize
data class MyMarker(
        val id: Long,
        val coordinates: LatLng?,
) : Parcelable
