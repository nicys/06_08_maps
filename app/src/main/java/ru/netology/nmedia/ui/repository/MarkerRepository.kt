package ru.netology.nmedia.ui.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.ui.dto.myMarker

interface MarkerRepository {
    fun getAll(): LiveData<List<myMarker>>
    fun add(myMarker: myMarker)
    fun remove(id: Long.Companion)
}