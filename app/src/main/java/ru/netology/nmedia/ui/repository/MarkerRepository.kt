package ru.netology.nmedia.ui.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.ui.dto.Marker

interface MarkerRepository {
    val data: LiveData<List<Marker>>

    suspend fun getAll()
    suspend fun save(marker: Marker)
    suspend fun removedById(id: Short)
}