package org.wit.valueGuitar.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.valueGuitar.helpers.exists
import org.wit.valueGuitar.helpers.read
import org.wit.valueGuitar.helpers.write

import timber.log.Timber
import java.lang.reflect.Type

import java.util.*

const val JSON_FILE = "guitars.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<GuitarModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class GuitarJSONStore(private val context: Context) : GuitarStore {

    var guitars = mutableListOf<GuitarModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<GuitarModel> {
        logAll()
        return guitars
    }

    override fun findById(id:Long) : GuitarModel? {
        val foundGuitar: GuitarModel? = guitars.find { it.id == id }
        return foundGuitar
    }

    override fun create(guitar: GuitarModel) {
        guitar.id = generateRandomId()
        guitars.add(guitar)
        serialize()
    }

    override fun update(guitar: GuitarModel) {
        val guitarList = findAll() as ArrayList<GuitarModel>
        var foundGuitar: GuitarModel? = guitarList.find { p -> p.id == guitar.id }
        if (foundGuitar != null) {
            foundGuitar.guitarMake = guitar.guitarMake
            foundGuitar.guitarModel = guitar.guitarModel
            foundGuitar.valuation = guitar.valuation
            foundGuitar.manufactureDate = guitar.manufactureDate
            foundGuitar.image = guitar.image
            foundGuitar.lat = guitar.lat
            foundGuitar.lng = guitar.lng
            foundGuitar.zoom = guitar.zoom
        }
        serialize()
    }

    override fun delete(guitar: GuitarModel) {
        guitars.remove(guitar)
        serialize()
    }
    private fun serialize() {
        val jsonString = gsonBuilder.toJson(guitars, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        guitars = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        guitars.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>, JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
