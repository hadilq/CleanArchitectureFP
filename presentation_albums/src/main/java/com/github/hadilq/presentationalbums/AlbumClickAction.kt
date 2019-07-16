package com.github.hadilq.presentationalbums

import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.presentationcommon.Action

data class AlbumClickAction(val album: Album) : Action