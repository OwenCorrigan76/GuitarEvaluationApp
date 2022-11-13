package org.wit.ValueGuitar.activities

import  android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.ValueGuitar.adapters.GuitarAdapter
import org.wit.ValueGuitar.adapters.GuitarListener
import org.wit.ValueGuitar.main.MainApp
import org.wit.ValueGuitar.models.GuitarModel
import org.wit.valueGuitar.R
import org.wit.valueGuitar.databinding.ActivityGuitarListBinding

class GuitarListActivity : AppCompatActivity(), GuitarListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivityGuitarListBinding

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
        binding.recyclerView.adapter = GuitarAdapter(app.guitars.findAll(), this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, ValueGuitarActivity::class.java)
                startActivityForResult(launcherIntent, 0)
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
        startActivityForResult(launcherIntent, 0)
    }

    /** This function will update the Card view */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}

