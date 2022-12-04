package org.wit.valueGuitar.room

/** this is to set up our database */
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.wit.valueGuitar.helpers.Converters
import org.wit.valueGuitar.models.GuitarModel

/** version number needs to be updates if we make changes to the database */
@Database(entities = arrayOf(GuitarModel::class), version = 1,  exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun guitarDao(): GuitarDao
}