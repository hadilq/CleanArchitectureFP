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

    override fun bindTo(data: BaseViewData) {
        track = (data as TrackViewData).track

        itemView.titleView.text = track?.title
        itemView.artistsView.text = track?.contributors?.fold("") { acc, a -> "$acc, ${a.name}" }?.removePrefix(", ")
    }
}