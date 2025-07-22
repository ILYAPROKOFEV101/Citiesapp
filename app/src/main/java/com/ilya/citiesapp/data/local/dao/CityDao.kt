package com.ilya.citiesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ilya.citiesapp.data.local.entity.CityEntity
import com.ilya.citiesapp.data.local.entity.CityListCrossRef
import com.ilya.citiesapp.data.local.entity.CityListEntity
import com.ilya.citiesapp.data.local.relations.CityListWithCities

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCity(city: CityEntity): Long

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCityList(cityList: CityListEntity): Long

    @Insert
    suspend fun insertCrossRef(crossRef: CityListCrossRef)

    @Transaction
    @Query("SELECT * FROM city_lists")
    suspend fun getAllCityLists(): List<CityListWithCities>

    @Query("SELECT * FROM cities")
    suspend fun getAllCities(): List<CityEntity>

    @Query("DELETE FROM city_list_cross_ref WHERE listId = :listId")
    suspend fun deleteCrossRefsByList(listId: Int)

    @Delete
    suspend fun deleteCityList(list: CityListEntity)
}