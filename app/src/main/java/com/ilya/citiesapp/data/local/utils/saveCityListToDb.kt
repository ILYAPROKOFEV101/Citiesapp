package com.ilya.citiesapp.data.local.utils

import android.content.Context
import com.ilya.citiesapp.data.local.db.AppDatabase
import com.ilya.citiesapp.data.local.entity.CityListCrossRef
import com.ilya.citiesapp.data.local.mapper.toEntity
import com.ilya.citiesapp.data.model.CityList

suspend fun saveCityListToDb(context: Context, list: CityList) {
    val db = AppDatabase.getInstance(context)
    val dao = db.cityDao()

    val cityIds = list.cities.map { city ->
        val entity = city.toEntity()
        dao.insertCity(entity)
    }

    val listId = dao.insertCityList(list.toEntity())

    cityIds.forEach { cityId ->
        dao.insertCrossRef(CityListCrossRef(listId.toInt(), cityId.toInt()))
    }
}
