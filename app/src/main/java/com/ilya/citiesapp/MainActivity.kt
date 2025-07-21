package com.ilya.citiesapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ilya.citiesapp.ui.CitiesFragment
import com.ilya.citiesapp.ui.MenuBottomSheet

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CitiesFragment())
            .commit()

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_cities -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CitiesFragment())
                        .commit()
                    true
                }
                R.id.nav_menu -> {
                    MenuBottomSheet().show(supportFragmentManager, "MenuBottomSheet")
                    false
                }
                else -> false
            }
        }
    }
}
