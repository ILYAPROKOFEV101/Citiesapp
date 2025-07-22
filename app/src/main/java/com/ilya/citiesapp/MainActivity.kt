package com.ilya.citiesapp

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
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

        // Загружаем начальный список
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

        // Парсим цвет из CityList с обработкой ошибок
        val parsedColor = try {
            Color.parseColor(list.colorHex)
        } catch (e: IllegalArgumentException) {
            Color.RED // Fallback цвет при ошибке
        }

        // Создаем ColorStateList с динамическими цветами
        val iconColors = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked), // Выбранное состояние
                intArrayOf(-android.R.attr.state_checked)  // Невыбранное состояние
            ),
            intArrayOf(
                parsedColor,    // Основной цвет из CityList
                Color.GRAY      // Серый для неактивных иконок
            )
        )

        // Применяем цвета к иконкам
        bottomNavigationView.itemIconTintList = iconColors

        // Обновляем заголовок (если нужно)
        bottomNavigationView.menu.findItem(R.id.nav_menu)?.title = list.shortName
    }


    fun createCircleDrawable(color: Int, sizeInDp: Int = 24): Drawable {
        val sizeInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            sizeInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        val bitmap = Bitmap.createBitmap(sizeInPx, sizeInPx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            isAntiAlias = true
            this.color = color
        }

        canvas.drawCircle(sizeInPx / 2f, sizeInPx / 2f, sizeInPx / 2f, paint)

        return BitmapDrawable(resources, bitmap).apply {
            setBounds(0, 0, sizeInPx, sizeInPx)
        }
    }

}
