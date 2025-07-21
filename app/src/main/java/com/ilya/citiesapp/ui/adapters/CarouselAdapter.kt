package com.ilya.citiesapp.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ilya.citiesapp.R
import com.ilya.citiesapp.data.CityList

class CarouselAdapter(
    private val items: List<CityList>,
    private val onItemSelected: (CityList) -> Unit,
    private val onAddNewClicked: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_ADD = 1
    }

    override fun getItemCount(): Int = items.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) TYPE_ADD else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_ITEM) {
            val view = inflater.inflate(R.layout.item_city_list, parent, false)
            ItemViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_add_button, parent, false)
            AddViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bind(items[position])
        } else if (holder is AddViewHolder) {
            holder.itemView.setOnClickListener { onAddNewClicked() }
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val shortName: TextView = itemView.findViewById(R.id.shortName)
        private val indicator: View = itemView.findViewById(R.id.colorIndicator)

        fun bind(cityList: CityList) {
            shortName.text = cityList.shortName
            indicator.setBackgroundColor(Color.parseColor(cityList.colorHex))
            itemView.setOnClickListener { onItemSelected(cityList) }
        }
    }

    inner class AddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
