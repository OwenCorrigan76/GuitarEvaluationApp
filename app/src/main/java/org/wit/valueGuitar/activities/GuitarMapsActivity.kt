package org.wit.guitar.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import org.wit.valueGuitar.main.MainApp
import org.wit.valueGuitar.databinding.ActivityGuitarMapsBinding
import org.wit.valueGuitar.databinding.ContentGuitarMapsBinding


class GuitarMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityGuitarMapsBinding
    private lateinit var contentBinding: ContentGuitarMapsBinding
    lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityGuitarMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        contentBinding = ContentGuitarMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }
    }

    fun configureMap() {
        map.setOnMarkerClickListener(this)
        map.uiSettings.setZoomControlsEnabled(true)

        app.guitars.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.guitarMake).title(it.guitarMake).position(loc)
            map.addMarker(options)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as Long
        val guitar = app.guitars.findById(tag)

        contentBinding.currentTitle.text = guitar!!.guitarMake
        contentBinding.currentDescription.text = guitar!!.guitarModel
        Picasso.get()
            .load(guitar.image)
            .into(contentBinding.imageView2)

        return true
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