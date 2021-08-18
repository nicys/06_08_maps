package ru.netology.nmedia.ui.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.ui.MarkerEntity
import ru.netology.nmedia.ui.dao.MarkerDao
import ru.netology.nmedia.ui.dto.Marker
import ru.netology.nmedia.ui.toDto

class MarkerRepositoryImpl(
    private val dao: MarkerDao
) : MarkerRepository {

    override val data = dao.getAll().map(List<MarkerEntity>::toDto)

    override suspend fun getAll() {
        try {
            dao.getAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun save(marker: Marker) {
        try {
            dao.save(MarkerEntity.fromDto(marker))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun removedById(id: Short) {
        try {
            dao.removedById(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

