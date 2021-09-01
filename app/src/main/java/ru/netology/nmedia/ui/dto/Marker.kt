package ru.netology.nmedia.ui.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Marker(
    val id: Int,
    val title: String,
    val coordinates: String,
) : Parcelable
