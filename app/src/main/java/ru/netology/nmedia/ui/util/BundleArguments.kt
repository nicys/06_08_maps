package ru.netology.nmedia.ui.util

import android.os.Bundle
import ru.netology.nmedia.ui.dto.Marker
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object MarkerArg : ReadWriteProperty<Bundle, Marker?> {
    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Marker?) {
        thisRef.putParcelable(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Marker? {
        return thisRef.getParcelable(property.name)
    }
}

object StringArg : ReadWriteProperty<Bundle, String?> {
    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
        thisRef.putString(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): String? {
        return thisRef.getString(property.name)
    }
}

object CoordinatesArg : ReadWriteProperty<Bundle, DoubleArray?> {
    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: DoubleArray?) {
        thisRef.putDoubleArray(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): DoubleArray? {
        return thisRef.getDoubleArray(property.name)
    }
}