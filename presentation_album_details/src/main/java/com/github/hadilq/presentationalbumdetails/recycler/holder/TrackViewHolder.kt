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
package com.github.hadilq.presentationalbumdetails.recycler.holder

import android.view.ViewGroup
import com.github.hadilq.cleanarchitecturefp.domain.entity.Track
import com.github.hadilq.presentationalbumdetails.R
import com.github.hadilq.presentationalbumdetails.recycler.data.BaseViewData
import com.github.hadilq.presentationalbumdetails.recycler.data.TrackViewData
import com.github.hadilq.presentationcommon.inflate
import kotlinx.android.synthetic.main.track.view.*
import javax.inject.Inject

class TrackViewHolder @Inject constructor(
    parent: ViewGroup
) : BaseViewHolder(parent.inflate(R.layout.track)) {

    private var track: Track? = null
    private var number: Int? = null

    override fun bindTo(data: BaseViewData) {
        (data as TrackViewData).let {
            track = it.track
            number = it.number
        }

        itemView.titleView.text = track?.title
        itemView.artistsView.text = track?.contributors?.fold("") { acc, a -> "$acc, ${a.name}" }?.removePrefix(", ")
        itemView.numberView.text = number?.toString()
    }
}