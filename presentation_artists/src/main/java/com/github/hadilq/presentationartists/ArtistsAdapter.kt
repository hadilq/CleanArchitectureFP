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
package com.github.hadilq.presentationartists

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import com.github.hadilq.presentationcommon.Action
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import javax.inject.Inject

class ArtistsAdapter(
    private val factory: ArtistViewHolderFactory,
    private val actionStream: (Observable<Action>) -> Unit
) : RecyclerView.Adapter<ArtistViewHolder>() {

    private val list = mutableListOf<Artist>()

    fun newList(l: List<Artist>) {
        list.clear()
        list.addAll(l)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        return factory.get(parent, actionStream)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bindTo(list[position])
    }

    class ArtistsAdapterFactory @Inject constructor(
        private val factory: ArtistViewHolderFactory
    ) {
        fun get(actionStream: (Observable<Action>) -> Unit) =
            ArtistsAdapter(factory, actionStream)
    }

    class ArtistViewHolderFactory @Inject constructor(
        private val picasso: Picasso
    ) {
        fun get(parent: ViewGroup, actionStream: (Observable<Action>) -> Unit) =
            ArtistViewHolder(parent, actionStream, picasso)
    }
}