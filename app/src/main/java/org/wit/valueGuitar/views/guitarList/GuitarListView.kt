package org.wit.valueGuitar.views.guitarList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.valueGuitar.R
import org.wit.valueGuitar.databinding.ActivityGuitarListBinding
import org.wit.valueGuitar.main.MainApp
import org.wit.valueGuitar.models.GuitarModel
import timber.log.Timber.i


class GuitarListView : AppCompatActivity(), GuitarListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGuitarListBinding
    lateinit var presenter: GuitarListPresenter

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuitarListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            binding.toolbar.title = "${title}: ${user.email}"
        }
        setSupportActionBar(binding.toolbar)
        presenter = GuitarListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        updateRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddGuitar() }
            R.id.item_map -> { presenter.doShowGuitarsMap() }
            R.id.item_logout -> { presenter.doLogout() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGuitarClick(gModel: GuitarModel) {
        presenter.doEditGuitar(gModel)

    }
    private fun updateRecyclerView(){
        GlobalScope.launch(Dispatchers.Main){
            binding.recyclerView.adapter =
                GuitarAdapter(presenter.getGuitars(), this@GuitarListView)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private suspend fun loadGuitars() {
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