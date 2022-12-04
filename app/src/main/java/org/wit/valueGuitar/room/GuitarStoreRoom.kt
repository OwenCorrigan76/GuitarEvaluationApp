package org.wit.valueGuitar.room

import android.content.Context
import androidx.room.Room

import org.wit.valueGuitar.models.GuitarModel
import org.wit.valueGuitar.models.GuitarStore

class GuitarStoreRoom(val context: Context) : GuitarStore {

    var dao: GuitarDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.guitarDao()
    }
/** suspend allows the function to be paused and resumed at a later time */

    override suspend fun findAll(): List<GuitarModel> {
        return dao.findAll()
    }

    override suspend fun findById(id: Long): GuitarModel? {
        return dao.findById(id)
    }

    override suspend fun create(guitar: GuitarModel) {
        dao.create(guitar)
    }

    override suspend fun update(guitar: GuitarModel) {
        dao.update(guitar)
    }

    override suspend fun delete(guitar: GuitarModel) {
        dao.deleteGuitar(guitar)
    }

    fun clear() {
    }
}