package ru.netology.nmedia.ui.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.ui.dto.Marker

interface MarkerRepository {
    val data: Flow<List<Marker>>
    suspend fun save(marker: Marker)
    suspend fun removeById(id: Int)
}