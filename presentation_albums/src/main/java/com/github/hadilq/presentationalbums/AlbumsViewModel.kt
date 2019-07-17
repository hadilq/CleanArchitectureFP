package com.github.hadilq.presentationalbums

import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.cleanarchitecturefp.domain.usecase.GetAlbums
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

class AlbumsViewModel @Inject constructor(
    private val usecase: GetAlbums
) : BaseViewModel() {

    private val albumsLiveData by lazy {
        val processor = BehaviorProcessor.create<List<Album>>()
        processor.offer(listOf())
        processor
    }

    private val networkErrorLiveData = BehaviorProcessor.create<Throwable>()
    private val openAlbumDetailsLiveEvent = PublishProcessor.create<Album>()

    fun albumsLiveData(): Flowable<List<Album>> = albumsLiveData.hide().observeOn(AndroidSchedulers.mainThread())
    fun networkErrorLiveData(): Flowable<Throwable> =
        networkErrorLiveData.hide().observeOn(AndroidSchedulers.mainThread())

    fun openAlbumDetailsLiveEvent(): Flowable<Album> =
        openAlbumDetailsLiveEvent.hide().observeOn(AndroidSchedulers.mainThread())

    fun startLoading(artistId: String) {
        Flowable.just(artistId)
            .compose(usecase.albums())
            .flatMap(::keepNewData)
            .subscribe()
            .track()
    }

    fun retry() {
        Flowable.just(Unit)
            .throttleFirst(1, TimeUnit.SECONDS)
            .compose(usecase.nextAlbums())
            .flatMap(::keepNewData)
            .subscribe()
            .track()
    }

    fun recyclerViewActions(stream: Observable<Action>) {
        stream
            .toFlowable(BackpressureStrategy.DROP)
            .throttleFirst(1, TimeUnit.SECONDS)
            .filterTo(AlbumClickAction::class.java)
            .map { it.album }
            .map { openAlbumDetailsLiveEvent.offer(it) }
            .subscribe()
            .track()
    }

    private fun keepNewData(pair: Pair<Flowable<Album>, Maybe<Throwable>>): Flowable<Unit> =
        Flowable.merge(
            Flowable.fromIterable(albumsLiveData.value).concatWith(pair.first)
                .toList().toFlowable().map { albumsLiveData.offer(it) }.map { Unit },
            pair.second
                .toFlowable().map { networkErrorLiveData.offer(it) }.map { Unit }
        )
}