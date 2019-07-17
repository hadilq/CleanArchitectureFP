package com.github.hadilq.presentationalbumdetails.recycler.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.hadilq.presentationalbumdetails.recycler.data.BaseViewData

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bindTo(data: BaseViewData)
}