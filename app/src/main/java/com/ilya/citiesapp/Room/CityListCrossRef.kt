package com.ilya.citiesapp.Room

import androidx.room.Entity

@Entity(
    tableName = "city_list_cross_ref",
    primaryKeys = ["listId", "cityId"]
)
data class CityListCrossRef(
    val listId: Int,
    val cityId: Int
)
