package com.github.hadilq.presentationalbumdetails

import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.cleanarchitecturefp.domain.entity.Track
import com.github.hadilq.cleanarchitecturefp.domain.usecase.GetAlbumDetails
import com.github.hadilq.presentationcommon.BaseViewModel
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

class AlbumDetailsViewModel @Inject constructor(
    private val usecase: GetAlbumDetails
) : BaseViewModel() {

    private val albumDetailsLiveData = BehaviorProcessor.create<Album>()
    private val tracksLiveData = BehaviorProcessor.create<List<Track>>()
    private val networkErrorLiveData = BehaviorProcessor.create<Throwable>()

    fun albumDetailsLiveData(): Flowable<Album> = albumDetailsLiveData.hide()
    fun tracksLiveData(): Flowable<List<Track>> = tracksLiveData.hide()
    fun networkErrorLiveData(): Flowable<Throwable> = networkErrorLiveData.hide()

    private var albumId: String? = null

    fun startLoading(id: String) {
        albumId = id
        Flowable.just(id)
            .compose(usecase.details())
            .flatMap(::keepNewData)
            .subscribe()
            .track()
    }

    fun retry() {
        albumId?.let(::startLoading)
    }

    private fun keepNewData(pair: Triple<Single<Album>, Flowable<Track>, Maybe<Throwable>>): Flowable<Unit> =
        Flowable.merge(
            pair.first
                .toFlowable().map { albumDetailsLiveData.offer(it) }.map { Unit },
            Flowable.fromIterable(tracksLiveData.value).concatWith(pair.second)
                .toList().toFlowable().map { tracksLiveData.offer(it) }.map { Unit },
            pair.third
                .toFlowable().map { networkErrorLiveData.offer(it) }.map { Unit }
        )
}