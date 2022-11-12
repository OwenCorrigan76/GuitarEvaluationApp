package org.wit.ValueGuitar.activities

import android.content.Intent
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

class ValueGuitarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityValueGuitarBinding
    lateinit var app: MainApp
    val gModel = GuitarModel() // get from the model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityValueGuitarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Activity started..")

      //  binding.valueBar.max = 50000
        binding.valuePicker.minValue = 500
        binding.valuePicker.maxValue = 50000

        binding.valuePicker.setOnValueChangedListener { _, _, newVal ->
            //Display the newly selected number to paymentAmount
            binding.valueAmount.setText("Valuation € $newVal")
        }

        binding.addGuitar.setOnClickListener() {
            // gModel = guitarModel, guitarMake = from model, guitarMakeAdd is the button
            gModel.guitarMake = binding.guitarMakeAdd.text.toString()
            gModel.guitarModel = binding.guitarModelAdd.text.toString()
             gModel.value = binding.valuePicker.value.toDouble()
            gModel.valuation = binding.valuePicker.value.toDouble()

            if (gModel.guitarMake.isNotEmpty()) {
                app.guitars.create(gModel.copy())

                setResult(RESULT_OK)
                finish()

                    i("add Button Pressed: ${gModel.guitarMake + gModel.guitarModel + gModel.value + gModel.valuation +  gModel.year}")
            } else {
                Snackbar
                    .make(it, "Please Enter Guitar Make", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_guitar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }
}
