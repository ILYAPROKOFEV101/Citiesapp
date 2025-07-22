package com.ilya.citiesapp.data.model

data class CityList(
    val shortName: String,
    val fullName: String,
    val colorHex: String,
    val cities: List<City>
)