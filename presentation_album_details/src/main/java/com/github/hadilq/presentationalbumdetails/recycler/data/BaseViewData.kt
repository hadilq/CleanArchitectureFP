package com.github.hadilq.presentationalbumdetails.recycler.data

abstract class BaseViewData {

    abstract val viewHolderType: Int

    enum class ViewType(val TYPE: Int) {
        TRACK(0), TITLE(1)
    }

    companion object {
        private val map = ViewType.values().associateBy(ViewType::TYPE)
        fun fromInt(type: Int): ViewType = map[type] ?: let { ViewType.TRACK }
    }
}