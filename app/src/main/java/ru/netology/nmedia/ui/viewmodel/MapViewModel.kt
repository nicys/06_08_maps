package ru.netology.nmedia.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import ru.netology.nmedia.ui.dto.Place

private val empty = Place(
    name = "",
    location = LatLng(0.0, 0.0)
)

class MapViewModel : ViewModel() {
    private val _places = MutableLiveData<List<Place>>()

    val places: LiveData<List<Place>>
        get() = _places
    private val _selectedPlace = MutableLiveData<Place>()
    val selectedPlace: LiveData<Place>
        get() = _selectedPlace

    fun selectPlace(place: Place) {
        _selectedPlace.value = place
    }
}
