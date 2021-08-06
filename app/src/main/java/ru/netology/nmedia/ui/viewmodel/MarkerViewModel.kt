package ru.netology.nmedia.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.ui.dto.MyMarker
import ru.netology.nmedia.ui.repository.MarkerRepository
import ru.netology.nmedia.ui.repository.MarkerRopositorySharedPref

private val empty = MyMarker(
        id = 0,
        coordinates = null,
        snippet = "-",
)

class MarkerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MarkerRepository = MarkerRopositorySharedPref(application)
    val data = repository.getAll()

    fun add(myMarker: MyMarker) {
        repository.add(myMarker)
    }

    fun remove(id: Long) = repository.remove(id)
}