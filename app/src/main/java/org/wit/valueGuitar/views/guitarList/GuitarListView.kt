package org.wit.valueGuitar.views.guitarList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.valueGuitar.R
import org.wit.valueGuitar.databinding.ActivityGuitarListBinding
import org.wit.valueGuitar.main.MainApp
import org.wit.valueGuitar.models.GuitarModel
import timber.log.Timber.i


class GuitarListView : AppCompatActivity(), GuitarListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGuitarListBinding
    lateinit var presenter: GuitarListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuitarListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = GuitarListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadGuitars()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddGuitar() }
            R.id.item_map -> { presenter.doShowGuitarsMap() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGuitarClick(gModel: GuitarModel) {
        presenter.doEditGuitar(gModel)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadGuitars() {
        binding.recyclerView.adapter = GuitarAdapter(presenter.getGuitars(),this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
    override fun onResume() {
        //update the view
        binding.recyclerView.adapter?.notifyDataSetChanged()
        i("recyclerView onResume")
        super.onResume()
    }
}