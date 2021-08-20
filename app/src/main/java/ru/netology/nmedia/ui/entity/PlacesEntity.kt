package ru.netology.nmedia.ui.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import ru.netology.nmedia.ui.dto.Place

@Entity
data class PlacesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Short,
    val name: String,
    @Embedded
    val location: LatLng,
) {
    fun toDto() = Place(
        id,
        name,
        location
    )

    companion object {
        fun fromDto(dto: Place) =
            PlacesEntity(
                dto.id,
                dto.name,
                dto.location,
            )
    }
}

fun List<PlacesEntity>.toDto(): List<Place> = map(PlacesEntity::toDto)
