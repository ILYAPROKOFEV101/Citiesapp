package com.ilya.citiesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilya.citiesapp.R
import com.ilya.citiesapp.data.City
import com.ilya.citiesapp.ui.adapters.CitiesAdapter
import com.ilya.citiesapp.ui.adapters.ItemTouchHelperCallback

class CitiesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CitiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.cities_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.citiesRecyclerView)
        adapter = CitiesAdapter(getCities())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val callback = ItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    private fun getCities(): MutableList<City> = mutableListOf(
        City("Париж", "III век до н. э."),
        City("Вена", "1147 год"),
        City("Берлин", "1237 год"),
        City("Варшава", "1321 год"),
        City("Милан", "1899 год")
    )
}
