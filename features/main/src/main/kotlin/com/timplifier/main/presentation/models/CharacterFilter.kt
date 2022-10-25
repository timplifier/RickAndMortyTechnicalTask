package com.timplifier.main.presentation.models

import android.os.Parcel
import android.os.Parcelable
import com.timplifier.core.extensions.write

data class CharacterFilter(
    var status: String? = "",
    var species: String? = "",
    var gender: String? = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.write(status)
        parcel.write(species)
        parcel.write(gender)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<CharacterFilter> {
        override fun createFromParcel(parcel: Parcel) =
            CharacterFilter(parcel)

        override fun newArray(size: Int): Array<CharacterFilter?> =
            arrayOfNulls(size)
    }
}