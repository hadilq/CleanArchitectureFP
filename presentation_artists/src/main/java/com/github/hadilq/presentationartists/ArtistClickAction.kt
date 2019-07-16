package com.github.hadilq.presentationartists

import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import com.github.hadilq.presentationcommon.Action

data class ArtistClickAction(val artist: Artist) : Action