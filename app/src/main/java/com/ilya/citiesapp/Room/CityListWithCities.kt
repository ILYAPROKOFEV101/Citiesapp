package com.ilya.citiesapp.Room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ilya.citiesapp.Room.Entity.CityEntity
import com.ilya.citiesapp.Room.Entity.CityListEntity

data class CityListWithCities(
    @Embedded val cityList: CityListEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CityListCrossRef::class,
            parentColumn = "listId",
            entityColumn = "cityId"
        )
    )
    val cities: List<CityEntity>
)
