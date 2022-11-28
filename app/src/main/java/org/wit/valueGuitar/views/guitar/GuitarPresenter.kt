package org.wit.valueGuitar.views.guitar
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.wit.valueGuitar.helpers.showImagePicker
import org.wit.valueGuitar.main.MainApp
import org.wit.valueGuitar.models.GuitarModel
import org.wit.valueGuitar.models.Location
import org.wit.valueGuitar.databinding.ActivityValueGuitarBinding
import org.wit.valueGuitar.views.location.EditLocationView
import timber.log.Timber


class GuitarPresenter(val view: GuitarView) {
    var gModel = GuitarModel()
    lateinit var app: MainApp
    lateinit var binding: ActivityValueGuitarBinding
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent> // initialise
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    val location = Location(52.245696, -7.131902, 15f)
    var edit = false;
    val today = Calendar.getInstance()
    val year = today.get(Calendar.YEAR)
    val month = today.get(Calendar.MONTH)
    val day = today.get(Calendar.DAY_OF_MONTH)

    init {
         binding = ActivityValueGuitarBinding.inflate(view.layoutInflater)
        app = view.application as MainApp
        if (view.intent.hasExtra("guitar_edit")) {
            edit = true
            gModel = view.intent.extras?.getParcelable("guitar_edit")!!
            view.showGuitar(gModel)
        }
        registerImagePickerCallback()
        registerMapCallback()

    }

    fun doAddOrSave(make: String, model: String, valuation: Double, manufactureDate: String) {
        gModel.guitarMake = make
        gModel.guitarModel = model
        gModel.valuation = valuation
        gModel.manufactureDate = manufactureDate
    //    gModel.image = image

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
        if (gModel.zoom != 0f) {
            location.lat = gModel.lat
            location.lng = gModel.lng
            location.zoom = gModel.zoom
        }
        val launcherIntent = Intent(view, EditLocationView::class.java) // come back to
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun cacheGuitar(make: String, model: String, valuation: Double, manufactureDate: String) {
        gModel.guitarMake = make;
        gModel.guitarModel = model
        gModel.valuation = valuation
        gModel.manufactureDate = manufactureDate
      //  gModel.image = image
    }

     fun doSetDatePicker(){
        val dialogP = DatePickerDialog(
            this,
            { _, Year, Month, Day ->
                val Month = Month + 1
                binding.dateView.setText("$Day/$Month/$Year")
            }, year, month, day
        )
        dialogP.show()
    }
    // displays today's date

    private fun registerImagePickerCallback() {

        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            gModel.image = result.data!!.data!!
                            view.updateImage(gModel.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
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
                            val location =
                                result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            gModel.lat = location.lat
                            gModel.lng = location.lng
                            gModel.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }
}
