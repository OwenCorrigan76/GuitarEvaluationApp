package org.wit.ValueGuitar.models

data class GuitarModel(
    var guitarMake: String = "",
    var guitarModel: String = "",
    var value: Double = 0.0,
    var valuation: Double = 0.0,
    var id: Long = 0,
    val year: Int = 0,
    val serialNumber: Long = 0L,
)
