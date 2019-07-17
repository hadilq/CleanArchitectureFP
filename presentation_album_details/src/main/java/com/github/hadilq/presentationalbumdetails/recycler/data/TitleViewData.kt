package com.github.hadilq.presentationalbumdetails.recycler.data

import com.github.hadilq.cleanarchitecturefp.domain.entity.Album

data class TitleViewData(
    val album: Album,
    override val viewHolderType: Int = BaseViewData.ViewType.TITLE.TYPE
) : BaseViewData()