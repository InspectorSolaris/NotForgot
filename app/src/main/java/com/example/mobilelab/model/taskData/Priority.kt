package com.example.mobilelab.model.taskData

import android.os.Parcel
import android.os.Parcelable

data class Priority(
    val id: Int,
    val name: String,
    val color: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Priority> {
        override fun createFromParcel(parcel: Parcel): Priority {
            return Priority(parcel)
        }

        override fun newArray(size: Int): Array<Priority?> {
            return arrayOfNulls(size)
        }
    }
}