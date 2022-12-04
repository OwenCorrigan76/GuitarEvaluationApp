package org.wit.valueGuitar.views.guitarList

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.valueGuitar.main.MainApp
import org.wit.valueGuitar.models.GuitarModel
import org.wit.valueGuitar.views.guitar.GuitarView
import org.wit.valueGuitar.views.map.GuitarMapView


class GuitarListPresenter(val view: GuitarListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    init {
        app = view.application as MainApp
        registerMapCallback()
        registerRefreshCallback()
    }

    suspend fun getGuitars() = app.guitars.findAll()

    fun doAddGuitar() {
        val launcherIntent = Intent(view, GuitarView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }


    fun doEditGuitar(gModel: GuitarModel) {
        val launcherIntent = Intent(view, GuitarView::class.java)
        launcherIntent.putExtra("guitar_edit", gModel)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun doShowGuitarsMap() {
        val launcherIntent = Intent(view, GuitarMapView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                GlobalScope.launch(Dispatchers.Main){
                    getGuitars()
                }
            }
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }
}