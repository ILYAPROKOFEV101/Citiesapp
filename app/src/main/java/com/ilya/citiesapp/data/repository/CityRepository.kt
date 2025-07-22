package com.ilya.citiesapp.data.repository

import com.ilya.citiesapp.data.local.dao.CityDao
import com.ilya.citiesapp.data.local.entity.CityEntity
import com.ilya.citiesapp.data.local.entity.CityListCrossRef
import com.ilya.citiesapp.data.local.mapper.toEntity
import com.ilya.citiesapp.data.local.relations.CityListWithCities
import com.ilya.citiesapp.data.model.City
import com.ilya.citiesapp.data.model.CityList

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
            City("Милан", "1899 год"),
            City("Амстердам", "1275 год"),
            City("Мадрид", "865 год"),
            City("Рим", "753 год до н. э."),
            City("Прага", "9 век"),
            City("Лиссабон", "205 год до н. э.")
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