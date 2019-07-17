package com.github.hadilq.presentationalbumdetails.recycler.holder

import android.view.ViewGroup
import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.presentationalbumdetails.R
import com.github.hadilq.presentationalbumdetails.recycler.data.BaseViewData
import com.github.hadilq.presentationalbumdetails.recycler.data.TitleViewData
import com.github.hadilq.presentationcommon.inflate
import kotlinx.android.synthetic.main.title.view.*

class TitleViewHolder(parent: ViewGroup) : BaseViewHolder(parent.inflate(R.layout.title)) {

    private var album: Album? = null

    override fun bindTo(data: BaseViewData) {
        album = (data as TitleViewData).album

        itemView.albumTitleView.text = album?.title
        itemView.artistNameView.text = album?.artist?.name
    }
}