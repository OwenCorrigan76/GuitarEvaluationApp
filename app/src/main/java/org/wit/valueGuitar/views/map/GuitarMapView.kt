package org.wit.valueGuitar.views.map


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.valueGuitar.databinding.ActivityGuitarMapsBinding
import org.wit.valueGuitar.databinding.ContentGuitarMapsBinding
import org.wit.valueGuitar.main.MainApp
import org.wit.valueGuitar.models.GuitarModel


class GuitarMapView : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityGuitarMapsBinding
    private lateinit var contentBinding: ContentGuitarMapsBinding
    lateinit var app: MainApp
    lateinit var presenter: GuitarMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityGuitarMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        presenter = GuitarMapPresenter(this)

        contentBinding = ContentGuitarMapsBinding.bind(binding.root)

        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync {
            GlobalScope.launch(Dispatchers.Main) {
                presenter.doPopulateMap(it)
            }
        }
    }
    override fun onMarkerClick(marker: Marker): Boolean {
        GlobalScope.launch(Dispatchers.Main) {
            presenter.doMarkerSelected(marker)
        }
        return true
    }

    fun showGuitar(guitar: GuitarModel) {
        contentBinding.currentGuitarMake.text = guitar.guitarMake
        contentBinding.currentGuitarModel.text = guitar.guitarModel
        contentBinding.currentValuation.text = guitar.valuation.toString()
        contentBinding.currentManufactureDate.text = guitar.manufactureDate
        Picasso.get()
            .load(guitar.image)
            .into(contentBinding.imageView2)
    }


    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }
}