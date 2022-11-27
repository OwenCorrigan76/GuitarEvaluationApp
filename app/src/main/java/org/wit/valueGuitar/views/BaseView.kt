package org.wit.valueGuitar.views

import android.content.Intent

import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger
import org.wit.valueGuitar.models.GuitarModel
import org.wit.valueGuitar.views.guitar.GuitarView
import org.wit.valueGuitar.views.guitarList.GuitarListView
import org.wit.valueGuitar.views.location.EditLocationView
import org.wit.valueGuitar.views.map.GuitarMapView


val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
    LOCATION, GUITAR, MAPS, LIST
}

open abstract class BaseView() : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, GuitarListView::class.java)
        when (view) {
            VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.GUITAR -> intent = Intent(this, GuitarView::class.java)
            VIEW.MAPS -> intent = Intent(this, GuitarMapView::class.java)
            VIEW.LIST -> intent = Intent(this, GuitarListView::class.java)
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar) {
        toolbar.title = title
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showGuitar(guitar: GuitarModel) {}
    open fun showGuitars(guitars: List<GuitarModel>) {}
    open fun showProgress() {}
    open fun hideProgress() {}
}