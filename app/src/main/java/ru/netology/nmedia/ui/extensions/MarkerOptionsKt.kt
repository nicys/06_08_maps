package ru.netology.nmedia.ui.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import ru.netology.nmedia.ui.dto.Marker
import ru.netology.nmedia.ui.entity.MarkerEntity
import ru.netology.nmedia.ui.entity.toEntity

fun MarkerOptions.icon(drawable: Drawable) {
    val canvas = Canvas()
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    canvas.setBitmap(bitmap)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    icon(BitmapDescriptorFactory.fromBitmap(bitmap))
}

fun List<Marker>.toEntity(): List<MarkerEntity> = map(Marker::toEntity)
fun List<MarkerEntity>.toDto(): List<Marker> = map(MarkerEntity::toDto)