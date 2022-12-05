package org.wit.valueGuitar.main

import android.app.Application
import org.wit.valueGuitar.models.GuitarJSONStore
import org.wit.valueGuitar.models.GuitarMemStore
import org.wit.valueGuitar.models.GuitarStore
import org.wit.valueGuitar.room.GuitarStoreRoom
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