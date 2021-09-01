package ru.netology.nmedia.ui.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Marker(
    val id: Int = 0,
    val title: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
) : Parcelable
