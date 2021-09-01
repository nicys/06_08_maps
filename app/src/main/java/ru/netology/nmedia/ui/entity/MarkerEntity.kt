package ru.netology.nmedia.ui.entity

import androidx.room.PrimaryKey
import ru.netology.nmedia.ui.dto.Marker

data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val latitude: Double,
    val longitude: Double,
) {
    fun toDto() = Marker(
        id,
        title,
        latitude,
        longitude
    )
}

fun Marker.toEntity() = MarkerEntity(
    id,
    title,
    latitude,
    longitude
)
