package ru.netology.nmedia.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import ru.netology.nmedia.ui.dto.Marker
import ru.netology.nmedia.ui.repository.MarkerRepository
import ru.netology.nmedia.ui.repository.MarkerRopositorySharedPref

private val empty = Marker(
    id = 0L,
    coordinates = null,
    snippet = "-",
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

    fun changeSnippet(snippet: String) {
        val text = snippet.trim()
        if (edited.value?.snippet == text) {
            return
        }
        edited.value = edited.value?.copy(snippet = text)
    }

    fun changeSnippetString(snippet: String): String {
        val text = snippet.trim()
        if (edited.value?.snippet == text) {
            return text
        }
        edited.value = edited.value?.copy(snippet = text)
        return snippet
    }
}