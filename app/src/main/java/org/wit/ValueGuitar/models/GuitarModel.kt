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
    val year: Int = 0,
    val serialNumber: Long = 0L,
    var image: Uri = Uri.EMPTY,  // default value is empty Uri
) : Parcelable
