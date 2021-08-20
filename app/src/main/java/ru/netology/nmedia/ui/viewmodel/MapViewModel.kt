package ru.netology.nmedia.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import ru.netology.nmedia.ui.db.AppDb
import ru.netology.nmedia.ui.dto.Place
import ru.netology.nmedia.ui.model.FeedModel
import ru.netology.nmedia.ui.repository.PlaceRepository
import ru.netology.nmedia.ui.repository.PlaceRepositoryImpl

private val empty = Place(
    id = 0,
    name = "",
    location = LatLng(0.0, 0.0)
)

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PlaceRepository =
        PlaceRepositoryImpl(AppDb.getInstance(context = application).markerDao())
    private val _places = MutableLiveData<List<Place>>()

    val places: LiveData<List<Place>>
        get() = _places
    private val _selectedPlace = MutableLiveData<Place>()
    val selectedPlace: LiveData<Place>
        get() = _selectedPlace

    val data: LiveData<FeedModel> = repository.data.map(::FeedModel)

    private val _dataState = MutableLiveData<FeedModel>()
    val dataState: LiveData<FeedModel>
        get() = _dataState

    val edited = MutableLiveData(empty)

    fun selectPlace(place: Place) {
        _selectedPlace.value = place
    }

    init {
        loadPlaces()
    }

    fun loadPlaces() = viewModelScope.launch {
        try {
            repository.getAll()
            _dataState.value = FeedModel()
        } catch (e: Exception) {
            _dataState.value = FeedModel(error = true)
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
            }
        }
        edited.value = empty
    }

    fun changeName(name: String) {
        val text = name.trim()
        if (edited.value?.name == text) {
            return
        }
        edited.value = edited.value?.copy(
            name = text
        )
    }

    fun deleteById(id: Short) {
        edited.value?.let {
            viewModelScope.launch {
                try {
                    repository.deleteById(id)
                    _dataState.value = FeedModel()
                } catch (e: Exception) {
                    _dataState.value = FeedModel(error = true)
                }
            }
        }
        edited.value = empty
    }

    fun edit(place: Place) {
        edited.value = place
    }
}
