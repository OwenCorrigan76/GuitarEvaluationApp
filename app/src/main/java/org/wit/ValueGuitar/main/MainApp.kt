package org.wit.ValueGuitar.main

import android.app.Application
import org.wit.ValueGuitar.models.GuitarMemStore
import org.wit.ValueGuitar.models.GuitarModel
import org.wit.ValueGuitar.models.GuitarStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {
    val guitars = ArrayList<GuitarModel>() // send array to the model
    lateinit var guitarsStore: GuitarStore


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        guitarsStore = GuitarMemStore()

        i("Guitar Evaluation started")
    }
}