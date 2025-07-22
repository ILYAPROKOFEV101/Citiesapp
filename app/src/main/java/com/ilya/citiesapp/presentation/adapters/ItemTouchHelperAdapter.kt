package com.ilya.citiesapp.presentation.adapters

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
}
