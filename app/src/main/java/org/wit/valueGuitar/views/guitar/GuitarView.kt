package org.wit.valueGuitar.views.guitar

import android.app.DatePickerDialog
import android.content.res.Resources
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.valueGuitar.models.GuitarModel
import org.wit.valueGuitar.R
import org.wit.valueGuitar.databinding.ActivityValueGuitarBinding
import timber.log.Timber.i

class GuitarView : AppCompatActivity() {
    private lateinit var binding: ActivityValueGuitarBinding
    private lateinit var presenter: GuitarPresenter
    var gModel = GuitarModel()
    val today = Calendar.getInstance()
    val year = today.get(Calendar.YEAR)
    val month = today.get(Calendar.MONTH)
    val day = today.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityValueGuitarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)


        binding.valuePicker.minValue = 500
        binding.valuePicker.maxValue = 10000
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
                                arrayAdapter.getPosition(gModel.guitarMake)
                            spinner.setSelection(spinnerPosition)
                        }
                        make.text = " ${types.get(position)}"
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        make.text = "please select a guitar make"
                    }
                }
        }
        /** Create a Presenter object*/
        presenter = GuitarPresenter(this)

        binding.chooseImage.setOnClickListener {
            /* presenter.cacheGuitar(
                binding.guitarMakeAdd.text.toString(),
                binding.guitarModelAdd.text.toString(),
                binding.valuePicker.value.toDouble(),
                binding.dateView.text.toString()
            )*/
            presenter.doSelectImage()
        }


        binding.guitarLocation.setOnClickListener {
            presenter.doSetLocation()
        }
        /* binding.addGuitar.setOnClickListener {
            presenter.cacheGuitar(
                binding.guitarMakeAdd.text.toString(),
                binding.guitarModelAdd.text.toString(),
                binding.valuePicker.value.toDouble(),
                binding.dateView.text.toString(),
                if (gModel.guitarMake.isEmpty()) {
                    Snackbar.make(
                        it,
                        "You Must Enter Guitar Make and valuation",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                } else {
                    presenter.doAddOrSave(
                        gModel.guitarMake,
                        gModel.guitarModel,
                        gModel.valuation,
                        gModel.manufactureDate
                    )
                }
                        i ("add Button Pressed: ${gModel.guitarMake + gModel.guitarModel + gModel.valuation + gModel.manufactureDate}")
                        setResult (RESULT_OK)
                        finish ())
        }*/

        binding.btnDatePicker.setOnClickListener {
            presenter.doSetDatePicker()
        }

        val toast = "Today's Date Is : $day/$month/$year"
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_guitar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> {
                if (binding.guitarMake.toString().isEmpty()) {
                    Snackbar.make(binding.root, R.string.enter_guitar_make, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    presenter.doAddOrSave(
                        binding.guitarMakeAdd.text.toString(),
                        binding.guitarModelAdd.text.toString(),
                        binding.valuePicker.value.toDouble(),
                        binding.dateView.text.toString()
                    )
                }
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }

        }
        return super.onOptionsItemSelected(item)
    }


    fun showGuitar(gModel: GuitarModel) {
        binding.guitarMakeAdd.setText(gModel.guitarMake)
        binding.guitarMakeAdd.setText(gModel.guitarModel)
        //  binding.addGuitar.setText(R.string.button_saveGuitar)
        binding.chooseImage.setText(R.string.change_guitar_image)
        binding.guitarLocation.setText(R.string.button_location)
        Picasso.get()
            .load(gModel.image)
            .into(binding.guitarImage)
        if (gModel.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_guitar_image)
        }
    }

    fun updateImage(image: Uri) {
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.guitarImage)
        binding.chooseImage.setText(R.string.change_guitar_image)
    }
}




