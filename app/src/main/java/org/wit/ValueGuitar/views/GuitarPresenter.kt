package org.wit.ValueGuitar.views

import android.content.Intent
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.wit.ValueGuitar.helpers.showImagePicker
import org.wit.ValueGuitar.main.MainApp
import org.wit.ValueGuitar.models.GuitarModel
import org.wit.ValueGuitar.models.Location
import org.wit.valueGuitar.databinding.ActivityValueGuitarBinding
import timber.log.Timber


class GuitarPresenter(private val view: GuitarView) {
    var gModel = GuitarModel()
    var app: MainApp = view.application as MainApp
    var binding: ActivityValueGuitarBinding =
        ActivityValueGuitarBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent> // initialise
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    var edit = false;

    init {
        if (view.intent.hasExtra("guitar_edit")) {
            edit = true
            gModel = view.intent.extras?.getParcelable("guitar_edit")!!
            view.showGuitar(gModel)
        }
        registerImagePickerCallback()
        registerMapCallback()

    } fun doAddOrSave(make: String, model: String) {
        gModel.guitarMake = make
        gModel.guitarModel = model
        if (edit) {
            app.guitars.update(gModel)
        } else {
            app.guitars.create(gModel)
        }

        view.finish()

    }

    fun doCancel() {
        view.finish()

    }

    fun doDelete() {
        app.guitars.delete(gModel)
        view.finish()

    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }

    fun doSetLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (gModel.zoom != 0f) {
            location.lat =  gModel.lat
            location.lng = gModel.lng
            location.zoom = gModel.zoom
        }
        val launcherIntent = Intent(view, Location::class.java) // come back to
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }
    fun cacheGuitar (make: String, model: String) {
        gModel.guitarMake = make;
        gModel.guitarModel = model
    }

    private fun registerImagePickerCallback() {

        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            gModel.image = result.data!!.data!!
                            view.updateImage(gModel.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            gModel.lat = location.lat
                            gModel.lng = location.lng
                            gModel.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }
}
