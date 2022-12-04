package org.wit.valueGuitar.models

/** This is an interface. The functions here must appear in the MemStore.
    This is implemented by those functions in MemStore.
    The interface gets from / adds to the Model.
*/

interface GuitarStore {
    suspend fun findAll() : List<GuitarModel>
    suspend fun findById(id: Long) : GuitarModel?
    suspend fun create(guitar: GuitarModel)
    suspend fun update(guitar: GuitarModel)
    suspend fun delete(guitar: GuitarModel)

}