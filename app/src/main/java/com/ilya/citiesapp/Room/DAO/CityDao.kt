package com.ilya.citiesapp.Room.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ilya.citiesapp.Room.CityListCrossRef
import com.ilya.citiesapp.Room.CityListWithCities
import com.ilya.citiesapp.Room.Entity.CityEntity
import com.ilya.citiesapp.Room.Entity.CityListEntity

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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
