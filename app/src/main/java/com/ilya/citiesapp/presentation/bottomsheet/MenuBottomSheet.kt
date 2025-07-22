package com.ilya.citiesapp.presentation.bottomsheet

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ilya.citiesapp.R
import com.ilya.citiesapp.data.model.CityList
import com.ilya.citiesapp.presentation.adapters.CarouselAdapter
import com.ilya.citiesapp.presentation.adapters.LimitedScrollLinearLayoutManager
import com.ilya.citiesapp.presentation.dialogs.AddCityListDialog
import com.ilya.citiesapp.presentation.viewmodel.CityViewModel

class MenuBottomSheet(
    private val onSelect: (CityList) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var viewModel: CityViewModel
    private lateinit var carousel: RecyclerView
    private lateinit var adapter: CarouselAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var snapHelper: LinearSnapHelper
    private lateinit var fullListNameText: TextView
    private lateinit var expandIndicator: ImageView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var nestedScrollView: NestedScrollView

    private var isScrollCorrectionInProgress = false

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.menu_bottom_sheet, container, false)

        carousel = view.findViewById(R.id.carouselRecyclerView)
        fullListNameText = view.findViewById(R.id.fullListName)
        expandIndicator = view.findViewById(R.id.expandIndicator)
        nestedScrollView = view.findViewById(R.id.bottomSheetContainer)

        viewModel = ViewModelProvider(requireActivity())[CityViewModel::class.java]

        layoutManager = LimitedScrollLinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        carousel.layoutManager = layoutManager

        snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(carousel)

        viewModel.cityLists.observe(viewLifecycleOwner) { lists ->
            adapter = CarouselAdapter(
                items = lists,
                onItemSelected = { list ->
                    val pos = lists.indexOf(list)
                    if (pos != -1) {
                        adapter.selectedPosition = pos
                        adapter.notifyDataSetChanged()
                        updateFullName(list)
                        onSelect(list)
                    }
                },
                onAddClicked = {
                    showAddDialog()
                }
            )
            carousel.adapter = adapter

            lists.firstOrNull()?.let {
                adapter.selectedPosition = 0
                adapter.notifyDataSetChanged()
                updateFullName(it)
                onSelect(it)
            }
        }

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
                        isScrollCorrectionInProgress = true
                        carousel.post {
                            carousel.smoothScrollToPosition(adapter.selectedPosition)
                            isScrollCorrectionInProgress = false
                        }
                    } else {
                        adapter.selectedPosition = pos
                        adapter.notifyDataSetChanged()
                        val selectedList = adapter.getItemAt(pos)
                        updateFullName(selectedList)
                        onSelect(selectedList)
                    }
                }
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomSheetBehavior()

        expandIndicator.setOnClickListener {
            toggleBottomSheetState()
        }

        setupScrollListener()
    }

    private fun setupBottomSheetBehavior() {
        val dialog = dialog as? BottomSheetDialog ?: return
        val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?: return

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                updateIndicatorIcon(newState)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun setupScrollListener() {
        nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            val isScrollingDown = scrollY > oldScrollY
            val isAtTop = !nestedScrollView.canScrollVertically(-1)

            if (isScrollingDown && isAtTop) {
                expandIndicator.setImageResource(R.drawable.stat_minus_2_24px)
            }
        }
    }

    private fun toggleBottomSheetState() {
        when (bottomSheetBehavior.state) {
            BottomSheetBehavior.STATE_EXPANDED -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
            else -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun updateIndicatorIcon(state: Int) {
        when (state) {
            BottomSheetBehavior.STATE_EXPANDED -> {
                expandIndicator.setImageResource(R.drawable.stat_minus_2_24px)
            }
            BottomSheetBehavior.STATE_HALF_EXPANDED,
            BottomSheetBehavior.STATE_COLLAPSED -> {
                expandIndicator.setImageResource(R.drawable.keyboard_double_arrow_up_24px)
            }
        }
    }

    private fun updateFullName(cityList: CityList) {
        fullListNameText.text = cityList.fullName
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
            it.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }
}