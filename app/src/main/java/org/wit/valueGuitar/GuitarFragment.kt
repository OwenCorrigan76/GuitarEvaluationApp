package org.wit.valueGuitar

import android.annotation.SuppressLint
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
import org.wit.valueGuitar.databinding.HomeBinding
import org.wit.valueGuitar.helpers.showImagePicker
import org.wit.valueGuitar.main.MainApp
import org.wit.valueGuitar.models.GuitarModel
import org.wit.valueGuitar.models.Location
import timber.log.Timber
import timber.log.Timber.i

class GuitarFragment : Fragment(R.layout.fragment_guitar) {
    lateinit var app: MainApp
    private var _fragBinding: FragmentGuitarBinding? = null
    private val fragBinding get() = _fragBinding!!
   // private lateinit var makeType: String
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    var guitar = GuitarModel()
    var edit = false
    val today = Calendar.getInstance()
    val year = today.get(Calendar.YEAR)
    val month = today.get(Calendar.MONTH)
    val day = today.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // app = activity?.application as MainApp
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentGuitarBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_guitar)

        fragBinding.valuePicker.minValue = 500
        fragBinding.valuePicker.maxValue = 10000
        fragBinding.valuePicker.setOnValueChangedListener { _, _, newVal ->
            /** Display the evaluation amount based on the valuePicker amount **/
            fragBinding.valueAmount.setText("Valuation € $newVal")
        }
/*
         val spinner = activity?.findViewById<Spinner>(R.id.guitarMake)
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
                                arrayAdapter.getPosition(guitar.guitarMake)
                            spinner.setSelection(spinnerPosition)
                        }
                        make?.text = " ${types.get(position)}"
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        make?.text = "please select a guitar make"
                    }
                }
        }*/

        app = activity?.application as MainApp
        i("Activity started..")

        if (activity?.intent?.hasExtra("guitar_edit") == true) {
            edit = true
            guitar = requireActivity().intent.extras?.getParcelable("guitar_edit")!!
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

     //   fillGuitarList()
        registerImagePickerCallback()
        registerMapCallback()
        setButtonListener(fragBinding)
        registerRefreshCallback()
        return root; }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }


    override fun onResume() {
        app.guitars.findAll()
        super.onResume()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_guitar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    fun setButtonListener(layout: FragmentGuitarBinding) {
        layout.addGuitar.setOnClickListener {
            guitar.guitarMake = fragBinding.guitarMakeAdd.text.toString()
            guitar.guitarModel = fragBinding.guitarModelAdd.text.toString()
            guitar.valuation = fragBinding.valuePicker.value.toDouble()
            guitar.value = fragBinding.valuePicker.value.toDouble()
            guitar.manufactureDate = fragBinding.dateView.text.toString()
            if (guitar.guitarMake.isEmpty()) {
                Snackbar.make(it, "Please add guitar", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.guitars.update(guitar.copy())
                }else{
                app.guitars.create(guitar.copy())}

                Timber.i("add Button Pressed: ${guitar}")
                activity?.setResult(AppCompatActivity.RESULT_OK)
                println("Break 1")
                activity?.finish()

                layout.chooseImage.setOnClickListener {
                    showImagePicker(imageIntentLauncher)
                    Timber.i("Select image")
                }

                layout.guitarLocation.setOnClickListener {
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

                fragBinding.chooseImage.setOnClickListener {
                    showImagePicker(imageIntentLauncher)
                }
                app.guitars.create(

                    GuitarModel(
                        guitarMake = guitar.guitarMake,
                        guitarModel = guitar.guitarModel,
                        valuation = guitar.valuation,
                        value = guitar.value,
                        image = guitar.image,
                        manufactureDate = guitar.manufactureDate
                    )
                )
              /*  val intent = Intent(app.applicationContext, GuitarListFragment::class.java)
                startActivity(intent)*/
                activity?.setResult(AppCompatActivity.RESULT_OK)
                println("Break 1")
                activity?.finish()
            }
        }

    }
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { GuitarListFragment}
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
/*    private fun fillGuitarList() {
        var spinner: Spinner = fragBinding.guitarMake
        var adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.guitar_make_list,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {
                makeType = spinner.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }*/
    companion object {
        @JvmStatic
        fun newInstance() =
            GuitarFragment().apply {
                arguments = Bundle().apply {
                }
            }

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

    /*  private fun registerListCallback() {
          refreshIntentLauncher =
              registerForActivityResult(ActivityResultContracts.StartActivityForResult())
              { result ->
                  when (result.resultCode) {
                      AppCompatActivity.RESULT_OK -> {
                          if (result.data != null) {
                              Timber.i("Guitar-Edit ${result.data.toString()}")
                              val guitar =
                                  result.data!!.extras?.getParcelable<GuitarModel>("guitar_edit")!!
                              Timber.i("Guitar == $guitar")
                              guitar.guitarMake = guitar.guitarMake
                              guitar.guitarModel = guitar.guitarModel
                              guitar.valuation = guitar.valuation
                              guitar.manufactureDate = guitar.manufactureDate
                          }
                      }
                      AppCompatActivity.RESULT_CANCELED -> {}
                      else -> {
                      }
                  }
              }

      }*/


}