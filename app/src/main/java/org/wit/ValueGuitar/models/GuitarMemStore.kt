package org.wit.ValueGuitar.models


/** This is where we communicate with the interface called GuitarStore.
    It must have the same functions that are declared in GuitarStore.
    The interface manages the Model. To access the information in the Model,
    we can now call MemStore instead.
*/

import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class GuitarMemStore : GuitarStore {

    val guitars = ArrayList<GuitarModel>()

    override fun findAll(): List<GuitarModel> {
        return guitars
    }

    override fun findById(id:Long) : GuitarModel? {
        val foundGuitar: GuitarModel? = guitars.find { it.id == id }
        return foundGuitar
    }

    override fun create(guitar: GuitarModel) {
        guitar.id = getId()
        guitars.add(guitar)
        logAll()
    }

    /** Update function. Used if the guitar object is edited */

    override fun update(guitar: GuitarModel) {
        var foundGuitar: GuitarModel? = guitars.find { p -> p.id == guitar.id }
        if (foundGuitar != null) {
            foundGuitar.guitarMake = guitar.guitarMake
            foundGuitar.guitarModel = guitar.guitarModel
            foundGuitar.valuation = guitar.valuation
            foundGuitar.image = guitar.image
            foundGuitar.lat = foundGuitar.lat
            foundGuitar.lng = foundGuitar.lng
            foundGuitar.zoom = foundGuitar.zoom
            logAll()
        }
    }
    fun logAll() {
        Timber.v("** Guitars List **")
        guitars.forEach { Timber.v("Guitar ${it}") }
    }
    override fun delete(guitar: GuitarModel) {
        guitars.remove(guitar)
    }
}