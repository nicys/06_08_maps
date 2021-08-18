package ru.netology.nmedia.ui

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import ru.netology.nmedia.ui.dto.Marker

@Entity
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val coordinates: LatLng?,
    val name: String,
) {
    fun toDto() = Marker(
        id,
        coordinates,
        name
    )

    companion object {
        fun fromDto(dto: Marker) =
            MarkerEntity(
                dto.id,
                dto.coordinates,
                dto.name
            )
    }
}

fun List<MarkerEntity>.toDto(): List<Marker> = map(MarkerEntity::toDto)
