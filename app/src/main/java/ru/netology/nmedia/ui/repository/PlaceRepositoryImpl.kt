package ru.netology.nmedia.ui.repository

import androidx.lifecycle.map
import ru.netology.nmedia.ui.dao.PlaceDao
import ru.netology.nmedia.ui.dto.Place
import ru.netology.nmedia.ui.entity.PlacesEntity
import ru.netology.nmedia.ui.entity.toDto

class PlaceRepositoryImpl(
    private val dao: PlaceDao
) : PlaceRepository {

    override val data = dao.getAll().map(List<PlacesEntity>::toDto)

    override suspend fun getAll() {
        try {
            dao.getAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override suspend fun save(place: Place) {
        try {
            dao.save(PlacesEntity.fromDto(place))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteById(id: Short) {
        try {
            dao.deleteById(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}