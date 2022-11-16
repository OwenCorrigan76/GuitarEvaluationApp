package org.wit.ValueGuitar.activities


import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Resources
import android.icu.util.Calendar
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import org.wit.ValueGuitar.helpers.showImagePicker
import org.wit.ValueGuitar.main.MainApp
import org.wit.ValueGuitar.models.GuitarModel
import org.wit.valueGuitar.R
import org.wit.valueGuitar.databinding.ActivityValueGuitarBinding
import timber.log.Timber.i
import com.squareup.picasso.Picasso
import org.wit.ValueGuitar.models.Location

/**
This is the ValueGuitar activity in the app.
 **/


class ValueGuitarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityValueGuitarBinding
    var gModel = GuitarModel() // get from the model
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent> // initialise
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var location = Location(52.245696, -7.139102, 15f)

    var edit = false

    val today = Calendar.getInstance()
    val year = today.get(Calendar.YEAR)
    val month = today.get(Calendar.MONTH)
    val day = today.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityValueGuitarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /**Below is our toolbar binded to @+id/toolbarAdd in layouts**/
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        binding.valuePicker.minValue = 500
        binding.valuePicker.maxValue = 50000
        binding.valuePicker.setOnValueChangedListener { _, _, newVal ->
            /** Display the evaluation amount based on the valuePicker amount **/
            binding.valueAmount.setText("Valuation € $newVal")
        }

        val spinner = findViewById<Spinner>(R.id.guitarMake)
        val make = findViewById<TextView>(R.id.guitarMakeAdd)
        val res: Resources = resources
        if (spinner != null) {
            val types = res.getStringArray(R.array.guitar_make_list)
            val arrayAdapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)
            spinner.adapter = arrayAdapter
            binding.guitarMake.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (make != null) {
                            val spinnerPosition =
                                arrayAdapter.getPosition( gModel.guitarMake)
                            spinner.setSelection(spinnerPosition)
                        }
                        make.text = " ${types.get(position)}"
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        make.text = "please select a guitar make"
                    }
                }
        }

        /** initialise app as MainApp **/
        app = application as MainApp
        i("Activity started..")


        /** parelize comes into effect here. If the guitar object has
        data from the ListActivity, populate it in this activity. It
        will use id to know which object is being passed and updated */

        if (intent.hasExtra("guitar_edit")) {
            edit = true
            gModel = intent.extras?.getParcelable("guitar_edit")!!
            binding.valuePicker.value.toString().toDouble()
            binding.valueAmount.setText("Valuation €" + gModel.valuation)
            binding.guitarMakeAdd.setText(gModel.guitarMake)
            binding.guitarModelAdd.setText(gModel.guitarModel)
            binding.addGuitar.setText(R.string.button_saveGuitar)
            binding.dateView.setText(gModel.manufactureDate)

            Picasso.get()
                .load(gModel.image)
                .into(binding.guitarImage)
            if (gModel.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_guitar_image)
            }
        }

        /** binding addGuitar button to event listener **/
        binding.addGuitar.setOnClickListener() {
            /** gModel = guitarModel, guitarMake = from model, guitarMakeAdd is the button **/
            gModel.guitarMake = binding.guitarMakeAdd.text.toString()
            gModel.guitarModel = binding.guitarModelAdd.text.toString()
            gModel.valuation = binding.valuePicker.value.toDouble()
            gModel.value = binding.valuePicker.value.toDouble()
            gModel.manufactureDate = binding.dateView.text.toString()
            if (gModel.guitarMake.isEmpty()) {
                Snackbar.make(it, "You Must Enter Guitar Make and valuation", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.guitars.update(gModel.copy())
                    /** use the create method from the MemStore. */
                } else {
                    app.guitars.create(gModel.copy())
                }
            }
            i("add Button Pressed: ${gModel.guitarMake + gModel.guitarModel + gModel.valuation + gModel.manufactureDate}")
            setResult(RESULT_OK)
            finish()
        }
        binding.btnDatePicker.setOnClickListener {
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
        val toast = "Today's Date Is : $day/$month/$year"
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.guitarLocation.setOnClickListener { // launch maps and pass location to MapActivity
            val location = Location(52.245696, -7.139102, 15f)
            if (gModel.zoom != 0f) {
                location.lat =  gModel.lat
                location.lng = gModel.lng
                location.zoom = gModel.zoom
            }

            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)

        }
        registerImagePickerCallback()
        registerMapCallback()
    }

    /** inflates the menu **/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_guitar, menu)
        if (edit && menu != null) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    /** cancel in menu_guitar **/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                app.guitars.delete(gModel)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**When the image is loaded, change the label*/
    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            gModel.image = result.data!!.data!!
                            Picasso.get()
                                .load(gModel.image)
                                .into(binding.guitarImage)
                            binding.chooseImage.setText(R.string.change_guitar_image)
                        }
                    }
                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            gModel.lat = location.lat
                            gModel.lng = location.lng
                            gModel.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}
