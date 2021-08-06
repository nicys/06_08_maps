package ru.netology.nmedia.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
//import ru.netology.nmedia.ui.dto.MyMarker
//import ru.netology.nmedia.ui.repository.MarkerRepository
//import ru.netology.nmedia.ui.repository.MarkerRopositorySharedPref

//private val empty = MyMarker(
//        id = 0L,
//        coordinates = null,
//        snippet = "-",
//)
//
//class MarkerViewModel(application: Application) : AndroidViewModel(application) {
//    private val repository: MarkerRepository = MarkerRopositorySharedPref(application)
//    val data = repository.getAll()
//
//    val edited = MutableLiveData(empty)
//
//    fun add() {
//        edited.value?.let {
//            repository.add(it)
//        }
//        edited.value = empty
//    }
//
//    fun remove(id: Long) = repository.remove(id)
//}