package org.wit.ValueGuitar.main

import android.app.Application
import org.wit.ValueGuitar.models.GuitarJSONStore
import org.wit.ValueGuitar.models.GuitarMemStore
import org.wit.ValueGuitar.models.GuitarStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

  lateinit var guitars: GuitarStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        guitars = GuitarJSONStore(applicationContext)
        i("Guitar app started")
    }
}