package org.wit.valueGuitar.views.location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import org.wit.valueGuitar.R
import org.wit.valueGuitar.databinding.ActivityMapBinding
import org.wit.valueGuitar.models.Location


class EditLocationView : AppCompatActivity(),
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener {
    private lateinit var binding: ActivityMapBinding
    private lateinit var map: GoogleMap
    lateinit var presenter: EditLocationPresenter
    var location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = EditLocationPresenter(this)

        location = intent.extras?.getParcelable<Location>("location")!!
        binding.forGoogleMap.onCreate(savedInstanceState)
        binding.forGoogleMap.getMapAsync{
            it.setOnMarkerDragListener(this)
            it.setOnMarkerClickListener(this)
            presenter.initMap(it)
        }
    }

    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
        binding.lat.setText("%.6f".format(marker.position.latitude))
        binding.lng.setText("%.6f".format(marker.position.longitude))
    }

    override fun onMarkerDragEnd(marker: Marker) {
        presenter.doUpdateLocation(marker.position.latitude,marker.position.longitude)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doUpdateMarker(marker)
        return false
    }

    override fun onBackPressed() {
        presenter.doOnBackPressed()

    }
    override fun onDestroy() {
        super.onDestroy()
        binding.forGoogleMap.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.forGoogleMap.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        binding.forGoogleMap.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.forGoogleMap.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.forGoogleMap.onSaveInstanceState(outState)
    }
}
