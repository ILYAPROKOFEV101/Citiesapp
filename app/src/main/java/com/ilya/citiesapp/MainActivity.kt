package com.ilya.citiesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ilya.citiesapp.data.CityList
import com.ilya.citiesapp.ui.CitiesFragment
import com.ilya.citiesapp.ui.MenuBottomSheet
import com.ilya.citiesapp.viewmodel.CityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CityViewModel
    private lateinit var navView: BottomNavigationView
    private var selectedList: CityList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[CityViewModel::class.java]
        navView = findViewById(R.id.bottom_navigation)

        // при первом запуске загружаем список
        viewModel.cityLists.observe(this) { lists ->
            if (lists.isNotEmpty() && selectedList == null) {
                selectedList = lists.first()
                openCitiesFragment(selectedList!!)
            }
        }

        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_cities -> {
                    selectedList?.let { list -> openCitiesFragment(list) }
                    true
                }
                R.id.nav_menu -> {
                    MenuBottomSheet { selected ->
                        selectedList = selected
                        openCitiesFragment(selected)
                    }.show(supportFragmentManager, "menu")
                    false
                }
                else -> false
            }
        }
    }

    private fun openCitiesFragment(list: CityList) {
        val fragment = CitiesFragment.newInstance(ArrayList(list.cities))
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
