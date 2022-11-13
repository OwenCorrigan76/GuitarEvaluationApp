package org.wit.ValueGuitar.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import org.wit.ValueGuitar.main.MainApp
import org.wit.ValueGuitar.models.GuitarModel
import org.wit.valueGuitar.R
import org.wit.valueGuitar.databinding.ActivityValueGuitarBinding
import timber.log.Timber.i

/**
This is the ValueGuitar activity in the app.
 **/


class ValueGuitarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityValueGuitarBinding
    var gModel = GuitarModel() // get from the model
    lateinit var app: MainApp

    var edit = false

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

        }
        /** binding addGuitar button to event listener **/
        binding.addGuitar.setOnClickListener() {
            /** gModel = guitarModel, guitarMake = from model, guitarMakeAdd is the button **/
            gModel.guitarMake = binding.guitarMakeAdd.text.toString()
            gModel.guitarModel = binding.guitarModelAdd.text.toString()
            gModel.valuation = binding.valuePicker.value.toDouble()
            gModel.value = binding.valuePicker.value.toDouble()

            if (gModel.guitarMake.isEmpty()) {
                Snackbar.make(it, "You Must Enter Guitar Make", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.guitars.update(gModel.copy())
                    /** use the create method from the MemStore. */
                } else {
                    app.guitars.create(gModel.copy())
                }
            }
            i("add Button Pressed: ${gModel.guitarMake + gModel.guitarModel + gModel.valuation + gModel.year}")
            setResult(RESULT_OK)
            finish()
        }

    }
        /** inflates the menu **/
        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            menuInflater.inflate(R.menu.menu_guitar, menu)
            return super.onCreateOptionsMenu(menu)
        }

        /** cancel in menu_guitar **/
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.item_cancel -> {
                    finish()
                }
            }
            return super.onOptionsItemSelected(item)
        }
    }


