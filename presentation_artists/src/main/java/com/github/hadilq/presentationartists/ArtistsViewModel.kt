package com.github.hadilq.presentationartists

import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import com.github.hadilq.cleanarchitecturefp.domain.usecase.SearchArtists
import com.github.hadilq.presentationcommon.Action
import com.github.hadilq.presentationcommon.BaseViewModel
import com.github.hadilq.presentationcommon.filterTo
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.processors.BehaviorProcessor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ArtistsViewModel @Inject constructor(
    private val usecase: SearchArtists
) : BaseViewModel() {

    private val artistsLiveData = BehaviorProcessor.create<List<Artist>>()
    private val networkErrorLiveData = BehaviorProcessor.create<Throwable>()

    fun artistsLiveData(): Flowable<List<Artist>> = artistsLiveData.hide()
    fun networkErrorLiveData(): Flowable<Throwable> = networkErrorLiveData.hide()

    fun searchViewActions(stream: Observable<CharSequence>) {
        stream
            .toFlowable(BackpressureStrategy.DROP)
            .throttleFirst(1, TimeUnit.SECONDS)
            .map(CharSequence::toString)
            .compose(usecase.findArtists())
            .flatMap(::keepNewData)
            .subscribe()
            .track()
    }

    fun recyclerViewActions(stream: Observable<Action>) {
        stream
            .toFlowable(BackpressureStrategy.DROP)
            .throttleFirst(1, TimeUnit.SECONDS)
            .filterTo(ArtistClickAction::class.java)
            .subscribe()
            .track()
    }

    fun retry() {
        Flowable.just(Unit)
            .throttleFirst(1, TimeUnit.SECONDS)
            .compose(usecase.findNextArtists())
            .flatMap(::keepNewData)
            .subscribe()
            .track()
    }

    private fun keepNewData(pair: Pair<Flowable<Artist>, Maybe<Throwable>>): Flowable<Unit> {
        return Flowable.merge(
            pair.first.toList()
                .toFlowable().flatMap { artistsLiveData }.map { Unit },
            pair.second
                .toFlowable().flatMap { networkErrorLiveData }.map { Unit }
        )
    }

}