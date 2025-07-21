package com.ilya.citiesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ilya.citiesapp.R
import com.ilya.citiesapp.data.CityList
import com.ilya.citiesapp.ui.adapters.CarouselAdapter
import com.ilya.citiesapp.viewmodel.CityViewModel

class MenuBottomSheet(
    private val onSelect: (CityList) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var viewModel: CityViewModel
    private lateinit var carousel: RecyclerView
    private lateinit var adapter: CarouselAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var snapHelper: LinearSnapHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.menu_bottom_sheet, container, false)
        carousel = view.findViewById(R.id.carouselRecyclerView)
        viewModel = ViewModelProvider(requireActivity())[CityViewModel::class.java]

        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        carousel.layoutManager = layoutManager

        snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(carousel)

        viewModel.cityLists.observe(viewLifecycleOwner) { lists ->
            adapter = CarouselAdapter(
                items = lists,
                onItemSelected = { list ->
                    val pos = lists.indexOf(list)
                    if (pos != -1 && pos < adapter.itemCount - 1) {
                        adapter.selectedPosition = pos
                        adapter.notifyDataSetChanged()
                        onSelect(list)
                    }
                },
                onAddClicked = {
                    showAddDialog()
                }
            )
            carousel.adapter = adapter
        }

        carousel.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView, state: Int) {
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    val centerView = snapHelper.findSnapView(layoutManager) ?: return
                    val pos = layoutManager.getPosition(centerView)
                    val isAddButton = pos == adapter.itemCount - 1

                    if (isAddButton) {
                        // нельзя выбрать "+", отскроллим назад
                        carousel.smoothScrollToPosition(adapter.selectedPosition)
                    } else {
                        adapter.selectedPosition = pos
                        adapter.notifyDataSetChanged()
                        val selectedList = adapter.getItemAt(pos)
                        onSelect(selectedList)
                    }
                }
            }
        })

        return view
    }

    private fun showAddDialog() {
        val available = viewModel.cityLists.value?.flatMap { it.cities }?.distinctBy { it.name } ?: emptyList()
        AddCityListDialog(available) { list ->
            viewModel.addList(list)
        }.show(parentFragmentManager, "AddCityListDialog")
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? BottomSheetDialog ?: return
        val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            it.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }
}
