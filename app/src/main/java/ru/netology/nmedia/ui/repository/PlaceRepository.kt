package ru.netology.nmedia.ui.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.ui.dto.Place

interface PlaceRepository {
    val data: LiveData<List<Place>>

    suspend fun getAll()
    suspend fun save(place: Place)
    suspend fun deleteById(id: Short)
}