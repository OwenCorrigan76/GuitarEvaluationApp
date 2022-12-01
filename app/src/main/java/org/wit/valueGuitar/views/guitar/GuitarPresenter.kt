package org.wit.valueGuitar.views.guitar

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.valueGuitar.helpers.showImagePicker
import org.wit.valueGuitar.main.MainApp
import org.wit.valueGuitar.models.GuitarModel
import org.wit.valueGuitar.models.Location
import org.wit.valueGuitar.databinding.ActivityValueGuitarBinding
import org.wit.valueGuitar.helpers.checkLocationPermissions
import org.wit.valueGuitar.helpers.createDefaultLocationRequest
import org.wit.valueGuitar.views.location.EditLocationView
import timber.log.Timber
import timber.log.Timber.i


class GuitarPresenter(private val view: GuitarView) {
    private val locationRequest = createDefaultLocationRequest()
    var gModel = GuitarModel()
    var map: GoogleMap? = null

    var app: MainApp = view.application as MainApp
    private lateinit var binding: ActivityValueGuitarBinding
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var edit = false;
    private val location = Location(52.245696, -7.139102, 15f)
    val today = Calendar.getInstance()
    val year = today.get(Calendar.YEAR)
    val month = today.get(Calendar.MONTH)
    val day = today.get(Calendar.DAY_OF_MONTH)

    init {
        doPermissionLauncher()
        registerImagePickerCallback()
        registerMapCallback()

        if (view.intent.hasExtra("guitar_edit")) {
            edit = true
            gModel = view.intent.extras?.getParcelable("guitar_edit")!!
            view.showGuitar(gModel)
        } else {
            /** If Guitar doesn't exist, Check Location Permissions. If permissions have not been granted */
            if (checkLocationPermissions(view)) {
                /** on screen dialog box */
                doSetCurrentLocation()
            }
            gModel.lat = location.lat
            gModel.lng = location.lng
        }
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
            locationUpdate(gModel.lat, gModel.lng)
        }
        val launcherIntent = Intent(view, EditLocationView::class.java) // come back to
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        /** passed from locationService*/
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(gModel.lat, gModel.lng)
    }


    /** updates the location marker on the map */
    fun locationUpdate(lat: Double, lng: Double) {
        gModel.lat = lat
        gModel.lng = lng
        gModel.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options =
            MarkerOptions().title(gModel.guitarMake).position(LatLng(gModel.lat, gModel.lng))
        map?.addMarker(options)
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(gModel.lat, gModel.lng),
                gModel.zoom
            )
        )
        view?.showGuitar(gModel)
    }

    fun cacheGuitar(make: String, model: String, manufactureDate: String) {
        gModel.guitarMake = make;
        gModel.guitarModel = model
    //    gModel.valuation = valuation
        gModel.manufactureDate = manufactureDate
        //  gModel.image = image
    }

  /*  fun doSetDatePicker() {
        val dialogP = DatePickerDialog(
            view,
            { _, Year, Month, Day ->
                val Month = Month + 1
                binding.dateView.setText("$Day/$Month/$Year")
            }, year, month, day
        )
        dialogP.show()
    }
    // displays today's date*/

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

    /** Callback functions */

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

    private fun doPermissionLauncher() {
        i("permission check called")
        requestPermissionLauncher =
                /** Returns an array of Strings, not really an activity */
            view.registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                    doSetCurrentLocation()
                } else {
                    /** set defaults */
                    locationUpdate(location.lat, location.lng)
                }
            }
    }

}
