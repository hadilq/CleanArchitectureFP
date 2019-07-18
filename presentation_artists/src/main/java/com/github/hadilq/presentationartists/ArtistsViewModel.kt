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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ArtistsViewModel @Inject constructor(
    private val usecase: SearchArtists
) : BaseViewModel() {

    private val artistsLiveData by lazy {
        val processor = BehaviorProcessor.create<List<Artist>>()
        processor.offer(listOf())
        processor
    }
    private val networkErrorLiveData = BehaviorProcessor.create<Throwable>()
    private val openAlbumsLiveEvent = PublishProcessor.create<String>()

    fun artistsLiveData(): Flowable<List<Artist>> = artistsLiveData.hide().observeOn(AndroidSchedulers.mainThread())
    fun networkErrorLiveData(): Flowable<Throwable> =
        networkErrorLiveData.hide().observeOn(AndroidSchedulers.mainThread())

    fun openAlbumsLiveEvent(): Flowable<String> = openAlbumsLiveEvent.hide().observeOn(AndroidSchedulers.mainThread())

    fun searchViewActions(stream: Observable<CharSequence>) {
        stream
            .toFlowable(BackpressureStrategy.DROP)
            .throttleFirst(1, TimeUnit.SECONDS)
            .map(CharSequence::toString)
            .filter { it.isNotBlank() }
            .compose(usecase.findArtists())
            .flatMap(::keepNewData)
            .observe()
    }

    fun retry() {
        Flowable.just(Unit)
            .throttleFirst(1, TimeUnit.SECONDS)
            .compose(usecase.findNextArtists())
            .flatMap(::keepNewData)
            .observe()
    }

    fun recyclerViewActions(stream: Observable<Action>) {
        stream
            .toFlowable(BackpressureStrategy.DROP)
            .throttleFirst(1, TimeUnit.SECONDS)
            .filterTo(ArtistClickAction::class.java)
            .map { it.artist.id }
            .map { openAlbumsLiveEvent.offer(it) }
            .observe()
    }

    private fun keepNewData(pair: Pair<Flowable<Artist>, Maybe<Throwable>>): Flowable<Unit> =
        Flowable.merge(
            pair.first
                .toList().toFlowable().map { artistsLiveData.offer(it) }.map { Unit },
            pair.second
                .toFlowable().map { networkErrorLiveData.offer(it) }.map { Unit }
        )
}