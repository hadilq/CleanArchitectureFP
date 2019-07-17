package com.github.hadilq.presentationalbums

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration(
    private val offset: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutParams = view.layoutParams as GridLayoutManager.LayoutParams

        if (layoutParams.spanIndex % 2 == 0) {
            outRect.top = offset
            outRect.right = offset
            outRect.left = offset / 2
        } else {
            outRect.top = offset
            outRect.right = offset / 2
            outRect.left = offset
        }
    }
}