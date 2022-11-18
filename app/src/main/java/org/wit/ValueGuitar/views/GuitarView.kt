package org.wit.ValueGuitar.views

import android.net.Uri
import android.os.Bundle
import android.util.Log.i
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.ValueGuitar.models.GuitarModel
import org.wit.valueGuitar.R
import org.wit.valueGuitar.databinding.ActivityValueGuitarBinding
import timber.log.Timber

class GuitarView  : AppCompatActivity() {
    private lateinit var binding: ActivityValueGuitarBinding
    private lateinit var presenter: GuitarPresenter
    var gModel = GuitarModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityValueGuitarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        /** Create a Presenter object*/
        presenter = GuitarPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheGuitar(binding.addGuitar.text.toString(), binding.guitarMakeAdd.text.toString())
            presenter.doSelectImage()
        }

        binding.guitarLocation.setOnClickListener {
            presenter.cacheGuitar(binding.addGuitar.text.toString(), binding.guitarMakeAdd.text.toString())
            presenter.doSetLocation()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_guitar, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        if (presenter.edit){
            deleteMenu.setVisible(true)
        }
        else{
            deleteMenu.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> {
                if (binding.guitarMake.toString().isEmpty()) {
                    Snackbar.make(binding.root, R.string.hint_guitarMake, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    presenter.doAddOrSave(binding.guitarMake.toString(), binding.guitarModelAdd.text.toString())
                }
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun showGuitar(guitar: GuitarModel) {
        binding.guitarMakeAdd.setText(guitar.guitarMake)
        binding.guitarMakeAdd.setText(guitar.guitarModel)

        Picasso.get()
            .load(guitar.image)
            .into(binding.guitarImage)
        if (gModel.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_guitar_image)
        }

    }

    fun updateImage(image: Uri) {
        Timber.tag("Image").i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.guitarImage)
        binding.chooseImage.setText(R.string.change_guitar_image)
    }
}
