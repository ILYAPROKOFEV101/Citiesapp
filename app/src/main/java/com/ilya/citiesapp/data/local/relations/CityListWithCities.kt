package com.ilya.citiesapp.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ilya.citiesapp.data.local.entity.CityEntity
import com.ilya.citiesapp.data.local.entity.CityListCrossRef
import com.ilya.citiesapp.data.local.entity.CityListEntity

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