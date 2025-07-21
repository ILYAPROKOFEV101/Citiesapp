package com.ilya.citiesapp.Room.SaveMetod

import android.content.Context
import com.ilya.citiesapp.Room.AppDatabase
import com.ilya.citiesapp.Room.CityListCrossRef
import com.ilya.citiesapp.Room.toEntity
import com.ilya.citiesapp.data.CityList

suspend fun saveCityListToDb(context: Context, list: CityList) {
    val db = AppDatabase.getInstance(context)
    val dao = db.cityDao()

    // 1. Вставляем или находим города
    val cityIds = list.cities.map { city ->
        val entity = city.toEntity()
        dao.insertCity(entity) // можно доработать, чтобы не дублировать
    }

    // 2. Вставляем список
    val listId = dao.insertCityList(list.toEntity())

    // 3. Связываем
    cityIds.forEach { cityId ->
        dao.insertCrossRef(CityListCrossRef(listId.toInt(), cityId.toInt()))
    }
}
