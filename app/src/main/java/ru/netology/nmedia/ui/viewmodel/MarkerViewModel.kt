package ru.netology.nmedia.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.netology.nmedia.ui.db.AppDb
import ru.netology.nmedia.ui.dto.Marker
import ru.netology.nmedia.ui.repository.MarkerRepository
import ru.netology.nmedia.ui.repository.MarkerRepositoryImpl
import ru.netology.nmedia.ui.util.SingleLiveEvent

private var empty = Marker()

class MarkerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MarkerRepository = MarkerRepositoryImpl(
        AppDb.getInstance(application).markerDao()
    )

    val data = repository.data
        .catch { e: Throwable ->
            e.printStackTrace()
            _loadMarkerExceptionEvent.call()
        }

    private val _markerCreatedEvent = SingleLiveEvent<Unit>()
    val markerCreatedEvent: LiveData<Unit>
        get() = _markerCreatedEvent

    private val _loadMarkerExceptionEvent = SingleLiveEvent<Unit>()
    val loadMarkerExceptionEvent: LiveData<Unit>
        get() = _loadMarkerExceptionEvent

    private val _saveMarkerExceptionEvent = SingleLiveEvent<Unit>()
    val saveMarkerExceptionEvent: LiveData<Unit>
        get() = _saveMarkerExceptionEvent

    fun save() {
        empty.let {
            viewModelScope.launch {
                try {
                    repository.save(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                    _saveMarkerExceptionEvent.call()
                }
                _markerCreatedEvent.call()
            }
        }
        empty = Marker()
    }

    fun changeData(title: String, latitude: Double, longitude: Double) {
        empty = empty.copy(
            title = title,
            latitude = latitude,
            longitude = longitude
        )
    }

    fun changeTitle(title: String) {
        empty = empty.copy(
            title = title
        )
    }

    fun edit(marker: Marker) {
        empty = marker
    }

    fun removeById(id: Int) {
        viewModelScope.launch {
            try {
                repository.removeById(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}