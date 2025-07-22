package com.ilya.citiesapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilya.citiesapp.data.local.db.AppDatabase
import com.ilya.citiesapp.data.model.City
import com.ilya.citiesapp.data.model.CityList
import com.ilya.citiesapp.data.repository.CityRepository
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CityRepository
    private val _cityLists = MutableLiveData<List<CityList>>()
    val cityLists: LiveData<List<CityList>> = _cityLists

    init {
        val dao = AppDatabase.getInstance(application).cityDao()
        repository = CityRepository(dao)
        viewModelScope.launch {
            repository.initializeDefaultIfEmpty()
            loadLists()
        }
    }

    fun loadLists() {
        viewModelScope.launch {
            val data = repository.getAllCityLists()
            _cityLists.value = data.map {
                CityList(
                    shortName = it.cityList.shortName,
                    fullName = it.cityList.fullName,
                    colorHex = it.cityList.colorHex,
                    cities = it.cities.map { city -> City(city.name, city.foundation) }
                )
            }
        }
    }

    fun addList(list: CityList) {
        viewModelScope.launch {
            repository.insertCityList(list)
            loadLists()
        }
    }
}
