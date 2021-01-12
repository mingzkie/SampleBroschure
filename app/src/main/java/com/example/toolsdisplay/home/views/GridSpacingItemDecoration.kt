package com.example.toolsdisplay.home.views

import android.graphics.Rect
import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(private var spanCount : Int = 0,
                                private var spacing : Int = 0,
                                private var includeEdge : Boolean = false) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        var position : Int = parent.getChildAdapterPosition(view)
        var column : Int = position%spanCount

        if(includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }

    }

}