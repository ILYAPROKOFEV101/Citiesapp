package com.ilya.citiesapp.ui

import android.graphics.Rect
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
import com.ilya.citiesapp.ui.adapters.LimitedScrollLinearLayoutManager
import com.ilya.citiesapp.viewmodel.CityViewModel
class MenuBottomSheet(
    private val onSelect: (CityList) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var viewModel: CityViewModel
    private lateinit var carousel: RecyclerView
    private lateinit var adapter: CarouselAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var snapHelper: LinearSnapHelper

    private var isScrollCorrectionInProgress = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.menu_bottom_sheet, container, false)

        carousel = view.findViewById(R.id.carouselRecyclerView)
        viewModel = ViewModelProvider(requireActivity())[CityViewModel::class.java]

        layoutManager = LimitedScrollLinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        carousel.layoutManager = layoutManager

        snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(carousel)




        // Подставляем данные в адаптер
        viewModel.cityLists.observe(viewLifecycleOwner) { lists ->
            adapter = CarouselAdapter(
                items = lists,
                onItemSelected = { list ->
                    val pos = lists.indexOf(list)
                    if (pos != -1) {
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

            // При старте выбираем первый элемент по умолчанию
            adapter.selectedPosition = 0
            adapter.notifyDataSetChanged()
            onSelect(lists.firstOrNull() ?: return@observe)
        }

        // Добавляем ItemDecoration для отступов — центрируем первый и последний элемент
        carousel.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                if (position == 0) {
                    val itemWidth = view.layoutParams.width
                    val sidePadding = (parent.width / 2) - (itemWidth / 2)
                    outRect.left = sidePadding
                }
            }
        })

        carousel.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView, state: Int) {
                if (state == RecyclerView.SCROLL_STATE_IDLE && !isScrollCorrectionInProgress) {
                    val centerView = snapHelper.findSnapView(layoutManager) ?: return
                    val pos = layoutManager.getPosition(centerView)

                    val lastIndex = adapter.itemCount - 1
                    val isAddButton = pos == lastIndex

                    if (isAddButton) {
                        // НЕ позволяем + быть в центре, откатываем к selectedPosition
                        isScrollCorrectionInProgress = true
                        carousel.post {
                            carousel.smoothScrollToPosition(adapter.selectedPosition)
                            isScrollCorrectionInProgress = false
                        }
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
