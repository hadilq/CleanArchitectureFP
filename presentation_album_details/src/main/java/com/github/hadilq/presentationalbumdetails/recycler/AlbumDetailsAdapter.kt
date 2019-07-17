package com.github.hadilq.presentationalbumdetails.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.hadilq.presentationalbumdetails.recycler.data.BaseViewData
import com.github.hadilq.presentationalbumdetails.recycler.holder.BaseViewHolder
import com.github.hadilq.presentationalbumdetails.recycler.holder.TitleViewHolder
import com.github.hadilq.presentationalbumdetails.recycler.holder.TrackViewHolder
import javax.inject.Inject

class AlbumDetailsAdapter @Inject constructor() : RecyclerView.Adapter<BaseViewHolder>() {

    private val list = mutableListOf<BaseViewData>()

    fun add(viewData: BaseViewData) {
        list.add(viewData)
    }

    fun removeAll(type: BaseViewData.ViewType) =
        list.removeAll {
            it.viewHolderType == type.TYPE
        }

    fun addAt(index: Int, data: BaseViewData) =
        list.add(index, data)

    fun addAll(l: List<BaseViewData>) = list.addAll(l)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when (BaseViewData.fromInt(viewType)) {
            BaseViewData.ViewType.TRACK -> TrackViewHolder(parent)
            BaseViewData.ViewType.TITLE -> TitleViewHolder(parent)
        }

    override fun getItemViewType(position: Int): Int = list[position].viewHolderType

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.bindTo(list[position])

}