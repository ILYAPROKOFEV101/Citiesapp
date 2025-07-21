package com.ilya.citiesapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.ilya.citiesapp.data.City

class SelectCityAdapter(
    private val cities: List<City>,
    private val onCheckedChange: (City, Boolean) -> Unit
) : RecyclerView.Adapter<SelectCityAdapter.CityHolder>() {

    private val selected = mutableSetOf<City>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_multiple_choice, parent, false)
        return CityHolder(view)
    }

    override fun getItemCount(): Int = cities.size

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        val city = cities[position]
        val isSelected = selected.contains(city)

        holder.checkBox.text = "${city.name} — ${city.foundation}"
        holder.checkBox.isChecked = isSelected

        holder.itemView.setOnClickListener {
            val nowChecked = !holder.checkBox.isChecked
            holder.checkBox.isChecked = nowChecked

            if (nowChecked) selected.add(city)
            else selected.remove(city)

            onCheckedChange(city, nowChecked)
        }
    }

    class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckedTextView = itemView.findViewById(android.R.id.text1)
    }
}
