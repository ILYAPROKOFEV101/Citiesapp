package com.ilya.citiesapp.data.local.mapper

import com.ilya.citiesapp.data.local.entity.CityEntity
import com.ilya.citiesapp.data.local.entity.CityListEntity
import com.ilya.citiesapp.data.model.City
import com.ilya.citiesapp.data.model.CityList

fun City.toEntity() = CityEntity(name = name, foundation = foundation)

fun CityList.toEntity() = CityListEntity(
    shortName = shortName,
    fullName = fullName,
    colorHex = colorHex
)
