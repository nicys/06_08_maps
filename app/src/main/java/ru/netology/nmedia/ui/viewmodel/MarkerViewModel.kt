package ru.netology.nmedia.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.ui.dto.Marker
import ru.netology.nmedia.ui.repository.MarkerRepository
import ru.netology.nmedia.ui.repository.MarkerRopositorySharedPref

private val empty = Marker(
        id = 0,
        coordinates = null,
        name = "-",
)

class MarkerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MarkerRepository = MarkerRopositorySharedPref(application)
    val data = repository.getAll()

    fun add(marker: Marker) {
        repository.add(marker)
    }

    fun remove(id: Long) = repository.remove(id)
}