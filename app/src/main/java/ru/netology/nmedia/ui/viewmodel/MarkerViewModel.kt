package ru.netology.nmedia.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import ru.netology.nmedia.ui.dto.MyMarker
import ru.netology.nmedia.ui.repository.MarkerRepository
import ru.netology.nmedia.ui.repository.MarkerRopositorySharedPref

private val empty = MyMarker(
        id = 0L,
        coordinates = null,
//        snippet = "-",
)

class MarkerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MarkerRepository = MarkerRopositorySharedPref(application)
    val data = repository.getAll()

    val edited = MutableLiveData(empty)

    fun addMarker() {
        edited.value?.let {
            repository.addMarker(it)
        }
        edited.value = empty
    }

    fun removeMarker(coord: LatLng) = repository.removeMarker(coord)
}