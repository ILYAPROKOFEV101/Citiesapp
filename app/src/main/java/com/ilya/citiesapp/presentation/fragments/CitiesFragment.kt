package com.ilya.citiesapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilya.citiesapp.R
import com.ilya.citiesapp.data.model.City
import com.ilya.citiesapp.presentation.adapters.CitiesAdapter
import com.ilya.citiesapp.presentation.adapters.ItemTouchHelperCallback

class CitiesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CitiesAdapter

    companion object {
        private const val ARG_CITIES = "arg_cities"

        fun newInstance(cities: ArrayList<City>): CitiesFragment {
            val fragment = CitiesFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_CITIES, cities)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.cities_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.citiesRecyclerView)

        val cities = arguments?.getParcelableArrayList<City>(ARG_CITIES) ?: listOf()
        adapter = CitiesAdapter(cities.toMutableList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val touchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        touchHelper.attachToRecyclerView(recyclerView)
    }
}