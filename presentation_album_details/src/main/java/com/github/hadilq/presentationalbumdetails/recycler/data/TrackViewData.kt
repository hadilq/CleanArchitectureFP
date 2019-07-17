package com.github.hadilq.presentationalbumdetails.recycler.data

import com.github.hadilq.cleanarchitecturefp.domain.entity.Track

data class TrackViewData(
    val track: Track,
    override val viewHolderType: Int = BaseViewData.ViewType.TRACK.TYPE
) : BaseViewData()