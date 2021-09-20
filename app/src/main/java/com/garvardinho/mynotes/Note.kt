package com.garvardinho.mynotes

import android.os.Parcel
import android.os.Parcelable

class Note() : Parcelable {
    var title: String? = null
    var noteBody: String? = null
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0

    constructor(
        title: String?,
        noteBody: String?,
        year: Int,
        month: Int,
        day: Int) : this() {
        this.title = title
        this.noteBody = noteBody
        this.year = year
        this.month = month
        this.day = day
    }

    constructor(parcel: Parcel) : this() {
        this.title = parcel.readString()
        this.noteBody = parcel.readString()
        this.year = parcel.readInt()
        this.month = parcel.readInt()
        this.day = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(noteBody)
        parcel.writeInt(year)
        parcel.writeInt(month)
        parcel.writeInt(day)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}