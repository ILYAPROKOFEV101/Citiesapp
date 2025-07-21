package com.ilya.citiesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ilya.citiesapp.R
import com.ilya.citiesapp.data.CityList
import com.ilya.citiesapp.ui.adapters.CarouselAdapter
import com.ilya.citiesapp.viewmodel.CityViewModel

class MenuBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewModel: CityViewModel
    private lateinit var carousel: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.menu_bottom_sheet, container, false)
        viewModel = ViewModelProvider(requireActivity())[CityViewModel::class.java]
        carousel = view.findViewById(R.id.carouselRecyclerView)

        viewModel.cityLists.observe(viewLifecycleOwner) { lists ->
            carousel.adapter = CarouselAdapter(
                lists,
                onItemSelected = { list -> updateTabBar(list) },
                onAddNewClicked = { showAddDialog() }
            )
        }

        return view
    }

    private fun showAddDialog() {
        val allCities = viewModel.cityLists.value?.firstOrNull()?.cities ?: emptyList()
        AddCityListDialog(allCities) { newList ->
            viewModel.addList(newList)
        }.show(parentFragmentManager, "AddDialog")
    }

    private fun updateTabBar(list: CityList) {
        // Обновление иконки и имени в TabBar — зависит от реализации MainActivity
    }
}

