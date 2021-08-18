package ru.netology.nmedia.ui.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.ui.dto.Marker

interface MarkerRepository {
    fun getAll(): LiveData<List<Marker>>
    fun add(marker: Marker)
    fun remove(id: Long)
}