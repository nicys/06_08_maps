package ru.netology.nmedia.ui.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.ui.dto.MyMarker

interface MarkerRepository {
    fun getAll(): LiveData<List<MyMarker>>
    fun add(myMarker: MyMarker)
    fun remove(id: Long)
}