package ru.netology.nmedia.ui.entity

import androidx.room.PrimaryKey
import ru.netology.nmedia.ui.dto.Marker

data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val coordinates: String,
) {
    fun toDto() = Marker(
        id,
        title,
        coordinates
    )
}

fun Marker.toEntity() = MarkerEntity(
    id,
    title,
    coordinates
)
