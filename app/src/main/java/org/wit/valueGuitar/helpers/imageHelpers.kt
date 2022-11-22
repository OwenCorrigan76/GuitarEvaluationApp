package org.wit.valueGuitar.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import org.wit.valueGuitar.R

/** this is a function (not a class) for image picker. It will allow us to
    display images and access network storage framework.
 */
fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT) // to allow the creation of a new document
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_guitar_image.toString())
    intentLauncher.launch(chooseFile)
}

