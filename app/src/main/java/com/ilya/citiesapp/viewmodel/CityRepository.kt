package com.ilya.citiesapp.viewmodel

import com.ilya.citiesapp.Room.CityListCrossRef
import com.ilya.citiesapp.Room.CityListWithCities
import com.ilya.citiesapp.Room.DAO.CityDao
import com.ilya.citiesapp.Room.Entity.CityEntity
import com.ilya.citiesapp.Room.toEntity
import com.ilya.citiesapp.data.City
import com.ilya.citiesapp.data.CityList

class CityRepository(private val dao: CityDao) {

    suspend fun getAllCities(): List<CityEntity> = dao.getAllCities()

    suspend fun getAllCityLists(): List<CityListWithCities> = dao.getAllCityLists()

    suspend fun insertCityList(cityList: CityList) {
        val cityIds = cityList.cities.map { dao.insertCity(it.toEntity()) }
        val listId = dao.insertCityList(cityList.toEntity())

        cityIds.forEach { cityId ->
            dao.insertCrossRef(CityListCrossRef(listId.toInt(), cityId.toInt()))
        }
    }

    suspend fun initializeDefaultIfEmpty() {
        if (dao.getAllCities().isNotEmpty()) return

        val defaultCities = listOf(
            City("Париж", "III век до н. э."),
            City("Вена", "1147 год"),
            City("Берлин", "1237 год"),
            City("Варшава", "1321 год"),
            City("Милан", "1899 год")
        )

        val list = CityList(
            shortName = "EU",
            fullName = "Список городов в Европе",
            colorHex = "#4CAF50",
            cities = defaultCities
        )

        insertCityList(list)
    }
}
