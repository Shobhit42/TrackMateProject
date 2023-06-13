package com.example.trackmate.db

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracking_table")
data class Track(
    var img: Bitmap? = null,
    var timestamp: Long = 0L,   // this describe when our run was
    var avgSpeedInKMH: Float = 0f,
    var distanceInMeter: Int = 0,
    var timeInMillis: Long = 0L,   // how long our run was
    var caloriesBurned: Int = 0,
    var weekNumber: Int = 0,
    var mode: String?
) : Parcelable{
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readLong(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    ) {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(img, flags)
        parcel.writeLong(timestamp)
        parcel.writeFloat(avgSpeedInKMH)
        parcel.writeInt(distanceInMeter)
        parcel.writeLong(timeInMillis)
        parcel.writeInt(caloriesBurned)
        parcel.writeInt(weekNumber)
        parcel.writeString(mode)
        parcel.writeValue(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}
