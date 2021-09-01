package ru.netology.nmedia.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.ui.db.AppDb
import ru.netology.nmedia.ui.dto.Marker
import ru.netology.nmedia.ui.model.FeedModel
import ru.netology.nmedia.ui.repository.MarkerRepository
import ru.netology.nmedia.ui.repository.MarkerRepositoryImpl

private val empty = Marker(
    id = 0,
    title = "",
    coordinates = "",
)

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MarkerRepository =
        MarkerRepositoryImpl(AppDb.getInstance(context = application).markerDao())

    val data: Flow<FeedModel> = repository.data.map(::FeedModel)

    private val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            viewModelScope.launch {
                repository.save(it)
            }
        }
        edited.value = empty
    }

    fun changeTitle(title: String) {
        val text = title.trim()
        if (edited.value?.title == text) {
            return
        }
        edited.value = edited.value?.copy(
            title = text
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