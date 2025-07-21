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
    private val onAddClicked: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_ADD = 1
    }

    var selectedPosition = 0

    override fun getItemCount(): Int = items.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) TYPE_ADD else TYPE_ITEM
    }

    fun getItemAt(position: Int): CityList = items[position]

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
            val cityList = items[position]
            holder.bind(cityList, position == selectedPosition)
        } else {
            holder.itemView.setOnClickListener {
                onAddClicked()
            }
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val colorCircle: View = itemView.findViewById(R.id.colorCircle)

        fun bind(list: CityList, isSelected: Boolean) {
            colorCircle.setBackgroundColor(Color.parseColor(list.colorHex))
            val scale = if (isSelected) 1.4f else 1.0f
            itemView.scaleX = scale
            itemView.scaleY = scale

            itemView.setOnClickListener {
                onItemSelected(list)
            }
        }
    }

    inner class AddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
