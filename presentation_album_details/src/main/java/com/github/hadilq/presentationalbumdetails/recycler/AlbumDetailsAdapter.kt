/***
 * Copyright 2019 Hadi Lashkari Ghouchani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * */
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