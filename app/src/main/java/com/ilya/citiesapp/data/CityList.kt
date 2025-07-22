package com.ilya.citiesapp.data

data class CityList(
    val shortName: String,
    val fullName: String,
    val colorHex: String,
    val cities: List<City>
)
