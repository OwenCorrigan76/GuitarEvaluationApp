package org.wit.valueGuitar.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**     This is a class, just for storing data. It stores all the data that is used in the app.
Adding @parcelize generates Parcelable implementation. This allows us to pass objects
around from activity to activity.
 */

@Parcelize
@Entity
data class GuitarModel(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var guitarMake: String = "",
    var guitarModel: String = "",
    var value: Double = 0.0,
    var valuation: Double = 0.0,
    var manufactureDate: String = "",
    val serialNumber: Long = 0L,
    var image: Uri = Uri.EMPTY,  // default value is empty Uri
    @Embedded var location : Location = Location()
) : Parcelable

@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable