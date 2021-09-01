package ru.netology.nmedia.ui.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.nmedia.ui.dao.MarkerDao
import ru.netology.nmedia.ui.dto.Marker
import ru.netology.nmedia.ui.entity.toEntity
import ru.netology.nmedia.ui.extensions.toDto
import java.lang.Exception

class MarkerRepositoryImpl(
    private val dao: MarkerDao
) : MarkerRepository {

    override val data: Flow<List<Marker>> =
        dao.getAll()
            .map { it.toDto() }
            .flowOn(Dispatchers.Default)

    override suspend fun save(marker: Marker) {
        try {
            dao.save(marker.toEntity())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun removeById(id: Int) {
        try {
            dao.removedById(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}