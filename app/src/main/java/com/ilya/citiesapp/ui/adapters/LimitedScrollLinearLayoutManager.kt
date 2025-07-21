package com.ilya.citiesapp.ui.adapters

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LimitedScrollLinearLayoutManager(
    context: Context,
    orientation: Int,
    reverseLayout: Boolean
) : LinearLayoutManager(context, orientation, reverseLayout) {

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
        if (childCount == 0) return scrolled

        val lastView = getChildAt(childCount - 1) ?: return scrolled
        val lastPos = getPosition(lastView)

        // Если последний элемент полностью виден — запрещаем скроллить дальше
        if (lastPos == itemCount - 1) {
            val rightEdge = getDecoratedRight(lastView)
            if (rightEdge <= width) {
                return 0 // не скроллим
            }
        }

        return scrolled
    }
}
