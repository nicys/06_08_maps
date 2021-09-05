package ru.netology.nmedia.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val repository: MarkerRepository =
        MarkerRepositoryImpl(AppDb.getInstance(context = application).markerDao())

//    val data: Flow<FeedModel> = repository.data.map(::FeedModel)

    val data = repository.data
        .catch { e: Throwable ->
            e.printStackTrace()
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

    private val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            viewModelScope.launch {
                repository.save(it)
            }
        }
        edited.value = empty
    }

//    fun changeTitle(title: String) {
//        empty = empty.copy(
//            title = title
//        )
//    }

    fun changeTitle(title: String) {
        val text = title.trim()
        if (edited.value?.title == text) {
            return
        }
        edited.value = edited.value?.copy(
            title = text
        )
    }

    fun changeLatitude(latitude: Double) {
        empty = empty.copy(
            latitude = latitude
        )
    }

    fun changeLongitude(longitude: Double) {
        empty = empty.copy(
            longitude = longitude
        )
    }

    fun changeData(title: String, latitude: Double, longitude: Double) {
        empty = empty.copy(
            title = title,
            latitude = latitude,
            longitude = longitude
        )
    }

    fun removeById(id: Int) {
        edited.value?.let {
            viewModelScope.launch {
                try {
                    repository.removeById(id)
                } catch (e: Exception) {
                }
            }
        }
        edited.value = empty
    }

    fun edit(marker: Marker) {
        edited.value = marker
    }
}