package ru.netology.nmedia.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.ui.dto.myMarker
import ru.netology.nmedia.ui.repository.MarkerRepository
import ru.netology.nmedia.ui.repository.MarkerRopositorySharedPref

private val empty = myMarker(
        id = 0,
)

class MarkerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MarkerRepository = MarkerRopositorySharedPref(application)
    val data = repository.getAll()

    fun add(myMarker: myMarker) {
        repository.add(myMarker)
    }

    fun remove(id: Long.Companion) = repository.remove(id)
}