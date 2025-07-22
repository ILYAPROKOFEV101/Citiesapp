package com.ilya.citiesapp.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "city_list_cross_ref",
    primaryKeys = ["listId", "cityId"]
)
data class CityListCrossRef(
    val listId: Int,
    val cityId: Int
)