package org.wit.ValueGuitar.activities

import  android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.ValueGuitar.adapters.GuitarAdapter
import org.wit.ValueGuitar.adapters.GuitarListener
import org.wit.ValueGuitar.main.MainApp
import org.wit.ValueGuitar.models.GuitarModel
import org.wit.guitar.activities.GuitarMapsActivity
import org.wit.valueGuitar.R
import org.wit.valueGuitar.databinding.ActivityGuitarListBinding

class GuitarListActivity : AppCompatActivity(), GuitarListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivityGuitarListBinding
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuitarListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        app = application as MainApp


        /** Set up the recycler view. */
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        /** get all information from the findAll method in the MemStore
        and bind it to the recycler view. */

        loadGuitars()
        registerRefreshCallback()
        registerMapCallback()

    }

    /**Menus here in appBar */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, ValueGuitarActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, GuitarMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /** When we click a guitar object, we launch an intent to open ValueGuitarActivity.
    We also pass the data from the guitar object using parcelable using .putExtra.
    PutExtra (from the Intent Class) passed the object with the intent. We give it
    a name (tag) of guitar_edit so that we can find the parcelized data using the
    key (guitar-edit).The info passed will be updated in out ValueGuitarActivity
    (see intent.hasExtra).
     */
    override fun onGuitarClick(guitar: GuitarModel) {
        val launcherIntent = Intent(this, ValueGuitarActivity::class.java)
        launcherIntent.putExtra("guitar_edit", guitar)
        refreshIntentLauncher.launch(launcherIntent)
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadGuitars() }
    }

    private fun loadGuitars() {
        showGuitars(app.guitars.findAll())
    }


    private fun showGuitars(guitars: List<GuitarModel>) {
        binding.recyclerView.adapter = GuitarAdapter(guitars, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}

