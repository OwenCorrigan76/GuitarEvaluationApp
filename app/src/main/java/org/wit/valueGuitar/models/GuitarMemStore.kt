package org.wit.valueGuitar.models


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

    override suspend fun findAll(): List<GuitarModel> {
        return guitars
    }

    override suspend fun findById(id:Long) : GuitarModel? {
        val foundGuitar: GuitarModel? = guitars.find { it.id == id }
        return foundGuitar
    }

    override suspend fun create(guitar: GuitarModel) {
        guitar.id = getId()
        guitars.add(guitar)
        logAll()
    }

    /** Update function. Used if the guitar object is edited */

    override suspend fun update(guitar: GuitarModel) {
        var foundGuitar: GuitarModel? = guitars.find { p -> p.id == guitar.id }
        if (foundGuitar != null) {
            foundGuitar.guitarMake = guitar.guitarMake
            foundGuitar.guitarModel = guitar.guitarModel
            foundGuitar.valuation = guitar.valuation
            foundGuitar.image = guitar.image
            foundGuitar.location = foundGuitar.location
            logAll()
        }
    }
    fun logAll() {
        Timber.v("** Guitars List **")
        guitars.forEach { Timber.v("Guitar ${it}") }
    }
    override suspend fun delete(guitar: GuitarModel) {
        guitars.remove(guitar)
    }
}