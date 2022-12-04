package org.wit.valueGuitar.room

import androidx.room.*
import org.wit.valueGuitar.models.GuitarModel
/** Data Access Object for accessing a database */
/** Creating coroutines allows multithreading */
/** suspend allows multiThreading */
@Dao
interface GuitarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(guitar: GuitarModel)

    @Query("SELECT * FROM GuitarModel")
    suspend fun findAll(): List<GuitarModel>

    @Query("select * from GuitarModel where id = :id")
    suspend fun findById(id: Long): GuitarModel

    @Update
    suspend fun update(guitar: GuitarModel)

    @Delete
    suspend fun deleteGuitar(guitar: GuitarModel)
}