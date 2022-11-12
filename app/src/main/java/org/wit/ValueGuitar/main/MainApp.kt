package org.wit.ValueGuitar.main

import android.app.Application
import org.wit.ValueGuitar.models.GuitarMemStore

import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val guitars = GuitarMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Guitar App has started")
    }
}