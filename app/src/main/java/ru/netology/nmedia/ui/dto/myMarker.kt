package ru.netology.nmedia.ui.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng

@Parcelize
data class myMarker(
        val coordinates: LatLng,
        val snippet: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<myMarker> {
        override fun createFromParcel(parcel: Parcel): myMarker {
            return myMarker(parcel)
        }

        override fun newArray(size: Int): Array<myMarker?> {
            return arrayOfNulls(size)
        }
    }
}

annotation class Parcelize
