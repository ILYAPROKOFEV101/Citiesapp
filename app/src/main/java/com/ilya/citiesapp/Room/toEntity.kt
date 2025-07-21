package com.ilya.citiesapp.Room

import com.ilya.citiesapp.Room.Entity.CityEntity
import com.ilya.citiesapp.Room.Entity.CityListEntity
import com.ilya.citiesapp.data.City
import com.ilya.citiesapp.data.CityList

fun City.toEntity() = CityEntity(name = name, foundation = foundation)

fun CityList.toEntity() = CityListEntity(
    shortName = shortName,
    fullName = fullName,
    colorHex = colorHex
)
