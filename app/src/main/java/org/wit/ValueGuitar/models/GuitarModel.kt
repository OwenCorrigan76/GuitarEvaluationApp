package org.wit.ValueGuitar.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**     This is a class, just for storing data. I stores all the data that is used in the app.
        Adding @parcelize generates Parcelable implementation. This allows us to pass objects
        around from activity to activity.
 */

@Parcelize
data class GuitarModel(
    var guitarMake: String = "",
    var guitarModel: String = "",
    var value: Double = 0.0,
    var valuation: Double = 0.0,
    var id: Long = 0,
    var manufactureDate: String = "",
    val serialNumber: Long = 0L,
    var image: Uri = Uri.EMPTY,  // default value is empty Uri
    var lat : Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable