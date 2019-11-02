package com.example.mobilelab.model.taskData

import android.os.Parcel
import android.os.Parcelable

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val done: Int,
    val created: Long,
    val deadline: Long,
    var category: Category?,
    var priority: Priority?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readParcelable(Category::class.java.classLoader),
        parcel.readParcelable(Priority::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeInt(done)
        parcel.writeLong(created)
        parcel.writeLong(deadline)
        parcel.writeParcelable(category, flags)
        parcel.writeParcelable(priority, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}