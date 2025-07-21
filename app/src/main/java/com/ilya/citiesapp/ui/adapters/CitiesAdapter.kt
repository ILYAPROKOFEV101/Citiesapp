package com.ilya.citiesapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ilya.citiesapp.R
import com.ilya.citiesapp.data.City
import java.util.Collections

class CitiesAdapter(
    private val cities: MutableList<City>
) : RecyclerView.Adapter<CitiesAdapter.CityViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    override fun getItemCount(): Int = cities.size

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(cities, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameView: TextView = itemView.findViewById(R.id.cityName)
        private val foundationView: TextView = itemView.findViewById(R.id.cityFoundation)

        fun bind(city: City) {
            nameView.text = city.name
            foundationView.text = city.foundation
        }
    }
}
