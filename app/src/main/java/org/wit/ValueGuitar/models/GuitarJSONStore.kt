package org.wit.ValueGuitar.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.ValueGuitar.helpers.exists
import org.wit.ValueGuitar.helpers.read
import org.wit.ValueGuitar.helpers.write

import timber.log.Timber
import java.lang.reflect.Type

import java.util.*

const val JSON_FILE = "festivals.json"
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

    override fun findById(id: Long): GuitarModel? {
        TODO("Not yet implemented")
    }

    override fun create(guitar: GuitarModel) {
        guitar.id = generateRandomId()
        guitars.add(guitar)
        serialize()
    }

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
