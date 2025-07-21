package com.ilya.citiesapp.Room.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_lists")
data class CityListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val shortName: String,
    val fullName: String,
    val colorHex: String
)