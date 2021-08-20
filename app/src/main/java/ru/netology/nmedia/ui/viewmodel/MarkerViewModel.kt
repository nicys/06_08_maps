package ru.netology.nmedia.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import ru.netology.nmedia.ui.db.AppDb
import ru.netology.nmedia.ui.dto.Marker
import ru.netology.nmedia.ui.model.FeedModel
import ru.netology.nmedia.ui.repository.MarkerRepository
import ru.netology.nmedia.ui.repository.MarkerRepositoryImpl

private val empty = Marker(
    id = 0,
    coordinates = LatLng(0.0, 0.0),
    title = "***",
)

class MarkerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MarkerRepository =
        MarkerRepositoryImpl(AppDb.getInstance(context = application).markerDao())

    val data: LiveData<FeedModel> = repository.data.map(::FeedModel)

    private val _dataState = MutableLiveData<FeedModel>()

    val edited = MutableLiveData(empty)

    init {
        loadMarkers()
    }

    private fun loadMarkers() = viewModelScope.launch {
        try {
            repository.getAll()
            _dataState.value = FeedModel()
        } catch (e: Exception) {
            _dataState.value = FeedModel(error = true)
        }
    }

    fun changeNameOfMarker(title: String) {
        val text = title.trim()
        if (edited.value?.title == text) {
            return
        } else {
            edited.value = edited.value?.copy(
                title = text
            )
        }
    }

    fun save() {
        edited.value?.let {
            viewModelScope.launch {
                try {
                    repository.save(it)
                    _dataState.value = FeedModel()
                } catch (e: Exception) {
                    _dataState.value = FeedModel(error = true)
                }
                edited.value = empty
            }
        }
    }

    fun removedById(id: Short) {
        viewModelScope.launch {
            try {
                repository.removedById(id)
                _dataState.value = FeedModel()
            } catch (e: Exception) {
                _dataState.value = FeedModel(error = true)
            }
        }
    }

    fun edit(marker: Marker) {
        edited.value = marker
    }


}