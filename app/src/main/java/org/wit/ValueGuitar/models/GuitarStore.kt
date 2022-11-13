package org.wit.ValueGuitar.models

/** This is an interface. The functions here must appear in the MemStore.
    This is implemented by those functions in MemStore.
    The interface gets from / adds to the Model.
*/

interface GuitarStore {
    fun findAll() : List<GuitarModel>
    fun findById(id: Long) : GuitarModel?
    fun create(guitar: GuitarModel)
    fun update(guitar: GuitarModel)
  //  fun delete(guitar: GuitarModel)

}