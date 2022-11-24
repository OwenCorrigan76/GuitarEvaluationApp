package org.wit.valueGuitar

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Resources
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.valueGuitar.activities.Home
import org.wit.valueGuitar.activities.MapActivity
import org.wit.valueGuitar.databinding.FragmentGuitarBinding
import org.wit.valueGuitar.helpers.showImagePicker
import org.wit.valueGuitar.main.MainApp
import org.wit.valueGuitar.models.GuitarModel
import org.wit.valueGuitar.models.Location
import timber.log.Timber

class GuitarFragment : Fragment(R.layout.fragment_guitar) {
    lateinit var app: MainApp
    private var _fragBinding: FragmentGuitarBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>


    var guitar = GuitarModel()
    var edit = false
    /* val today = Calendar.getInstance()
     val year = today.get(Calendar.YEAR)
     val month = today.get(Calendar.MONTH)
     val day = today.get(Calendar.DAY_OF_MONTH)*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentGuitarBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_guitar)

        val intent = Intent(app.applicationContext, Home::class.java)
        //   startActivity(intent)

        fragBinding.valuePicker.minValue = 500
        fragBinding.valuePicker.maxValue = 10000
        fragBinding.valuePicker.setOnValueChangedListener { _, _, newVal ->
            /** Display the evaluation amount based on the valuePicker amount **/
            fragBinding.valueAmount.setText("Valuation € $newVal")
        }
        /* val spinner = activity?.findViewById<Spinner>(R.id.guitarMake)
         val make = activity?.findViewById<TextView>(R.id.guitarMakeAdd)
         val res: Resources = resources
         if (spinner != null) {
             val types = res.getStringArray(R.array.guitar_make_list)
             val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)
             spinner.adapter = arrayAdapter
             fragBinding.guitarMake.onItemSelectedListener =
                 object : AdapterView.OnItemSelectedListener {
                     override fun onItemSelected(
                         parent: AdapterView<*>,
                         view: View?,
                         position: Int,
                         id: Long
                     ) {
                         if (make != null) {
                             val spinnerPosition =
                                 arrayAdapter.getPosition( guitar.guitarMake)
                             spinner.setSelection(spinnerPosition)
                         }
                         make?.text = " ${types.get(position)}"
                     }

                     override fun onNothingSelected(parent: AdapterView<*>?) {
                         make?.text = "please select a guitar make"
                     }
                 }
         }*/

        if (intent.hasExtra("guitar_edit")) {
            edit = true
            guitar = intent.extras?.getParcelable("guitar_edit")!!
            fragBinding.valuePicker.value.toString().toDouble()
            fragBinding.valueAmount.setText("Valuation €" + guitar.valuation)
            fragBinding.guitarMakeAdd.setText(guitar.guitarMake)
            fragBinding.guitarModelAdd.setText(guitar.guitarModel)
            fragBinding.addGuitar.setText(R.string.button_saveGuitar)
            fragBinding.dateView.setText(guitar.manufactureDate)
            Picasso.get()
                .load(guitar.image)
                .into(fragBinding.guitarImage)
            if (guitar.image != Uri.EMPTY) {
                fragBinding.chooseImage.setText(R.string.change_guitar_image)
            }
        }

        setButtonListener(fragBinding)
        registerImagePickerCallback()
        registerMapCallback()
        return root; }

    companion object {
        @JvmStatic
        fun newInstance() =
            GuitarFragment().apply {
                arguments = Bundle().apply {
                }
            }

    }
/*    fun setButtonListener(binding: FragmentGuitarBinding) {
        binding.addGuitar.setOnClickListener {
            println("Pressed")
             app.guitars.create(
                GuitarModel(
                    guitarMake = binding.guitarMakeAdd.text.toString(),
                    guitarModel = binding.guitarModelAdd.text.toString(),
                    valuation = binding.valuePicker.value.toDouble(),
                    value = binding.valuePicker.value.toDouble(),
                    manufactureDate = binding.dateView.text.toString(),
                )
            )

        }
    }*/


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_guitar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    fun setButtonListener(binding: FragmentGuitarBinding) {
        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
            Timber.i("Select image")
        }

        binding.guitarLocation.setOnClickListener {
            Timber.i("Set Location Pressed")
            val location = Location(52.292, -6.497, 16f)
            if (guitar.zoom != 0f) {
                location.lat = guitar.lat
                location.lng = guitar.lng
                location.zoom = guitar.zoom
            }
            val launcherIntent = Intent(getActivity(), MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        binding.addGuitar.setOnClickListener() {
            guitar.guitarMake = fragBinding.guitarMakeAdd.text.toString()
            guitar.guitarModel = fragBinding.guitarModelAdd.text.toString()
            guitar.valuation = binding.valuePicker.value.toDouble()
            guitar.value = binding.valuePicker.value.toDouble()
            guitar.manufactureDate = binding.dateView.text.toString()
            if (guitar.guitarMake.isEmpty()) {
                Snackbar.make(it, "Please add guitar", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.guitars.update(guitar.copy())
                } else {
                    app.guitars.create(guitar.copy())
                }
            }
            Timber.i("add Button Pressed: ${guitar.guitarMake + guitar.guitarModel + guitar.valuation + guitar.manufactureDate}")
            activity?.setResult(AppCompatActivity.RESULT_OK)
            activity?.finish()

            fragBinding.chooseImage.setOnClickListener {
                showImagePicker(imageIntentLauncher)
            }
            app.guitars.create(
                GuitarModel(
                    guitarMake = guitar.guitarMake,
                    guitarModel = guitar.guitarModel,
                    valuation = guitar.valuation,
                    value =  guitar.value,
                    image = guitar.image,
                    manufactureDate = guitar.manufactureDate
                )

            )

        }

        /*  fragBinding.btnDatePicker.setOnClickListener {
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
      */
    }

    /**When the image is loaded, change the label*/
    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            guitar.image = result.data!!.data!!
                            Picasso.get()
                                .load(guitar.image)
                                .into(fragBinding.guitarImage)
                            fragBinding.chooseImage.setText(R.string.change_guitar_image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location =
                                result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            guitar.lat = location.lat
                            guitar.lng = location.lng
                            guitar.zoom = location.zoom
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {
                    }
                }
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }


    override fun onResume() {
        app.guitars.findAll()
        super.onResume()
    }
}