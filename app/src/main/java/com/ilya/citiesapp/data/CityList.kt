package com.ilya.citiesapp.data

data class CityList(
    val shortName: String,
    val fullName: String,
    val colorHex: String, // Например: "#4CAF50"
    val cities: List<City>
)
