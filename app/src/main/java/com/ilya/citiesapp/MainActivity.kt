package com.ilya.citiesapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ilya.citiesapp.data.model.CityList
import com.ilya.citiesapp.presentation.fragments.CitiesFragment
import com.ilya.citiesapp.presentation.bottomsheet.MenuBottomSheet
import com.ilya.citiesapp.presentation.viewmodel.CityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CityViewModel
    private lateinit var navView: BottomNavigationView
    private var selectedList: CityList? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>
    private lateinit var toggleButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[CityViewModel::class.java]
        navView = findViewById(R.id.bottom_navigation)

        viewModel.cityLists.observe(this) { lists ->
            if (lists.isNotEmpty() && selectedList == null) {
                selectedList = lists.first()
                openCitiesFragment(selectedList!!)
                updateTabBarAppearance(selectedList!!)
            }
        }

        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_cities -> {
                    selectedList?.let { list -> openCitiesFragment(list) }
                    false
                }
                R.id.nav_menu -> {
                    MenuBottomSheet { selected ->
                        selectedList = selected
                        openCitiesFragment(selected)
                        updateTabBarAppearance(selected)
                    }.show(supportFragmentManager, "menu")
                    true
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

    private fun updateTabBarAppearance(list: CityList) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val parsedColor = try {
            Color.parseColor(list.colorHex)
        } catch (e: IllegalArgumentException) {
            Color.RED
        }

        val iconColors = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                parsedColor,
                Color.GRAY
            )
        )

        bottomNavigationView.itemIconTintList = iconColors

        bottomNavigationView.menu.findItem(R.id.nav_menu)?.title = list.shortName
    }



}
