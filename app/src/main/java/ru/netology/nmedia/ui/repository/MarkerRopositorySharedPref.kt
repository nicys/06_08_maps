package ru.netology.nmedia.ui.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.ui.dto.myMarker

class MarkerRopositorySharedPref(
        context: Context,
) : MarkerRepository {
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, myMarker::class.java).type
    private val key = "markers"
    private var nextId = 1L
    private var markers = emptyList<myMarker>()
        set(value) {
            field = value
            sync()
        }
    private val data = MutableLiveData(markers)

    override fun getAll(): LiveData<List<myMarker>> = data

    init {
        prefs.getString(key, null)?.let {
            markers = gson.fromJson(it, type)
            nextId = markers.maxOf { post -> post.id } + 1
            data.value = markers
        }
    }

    private fun sync() {
        with(prefs.edit()) {
            putString(key, gson.toJson(markers))
            apply()
        }
    }

    override fun add(myMarker: myMarker) {
        if (myMarker.id == 0L) {
            markers = listOf(
                    myMarker.copy(
                            id = nextId++,
                    )
            ) + markers
            data.value = markers
            return
        }
    }

    override fun remove(id: Long.Companion) {
        markers = markers.map {
            if (it.id != it.id) it else it.copy(id = it.id)
        }
        data.value = markers
    }
}

