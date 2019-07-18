/***
 * Copyright 2019 Hadi Lashkari Ghouchani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * */
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
            .observe()
    }

    fun retry() {
        Flowable.just(Unit)
            .throttleFirst(1, TimeUnit.SECONDS)
            .compose(usecase.nextAlbums())
            .flatMap(::keepNewData)
            .observe()
    }

    fun recyclerViewActions(stream: Observable<Action>) {
        stream
            .toFlowable(BackpressureStrategy.DROP)
            .throttleFirst(1, TimeUnit.SECONDS)
            .filterTo(AlbumClickAction::class.java)
            .map { it.album }
            .map { openAlbumDetailsLiveEvent.offer(it) }
            .observe()
    }

    private fun keepNewData(pair: Pair<Flowable<Album>, Maybe<Throwable>>): Flowable<Unit> =
        Flowable.merge(
            Flowable.fromIterable(albumsLiveData.value).concatWith(pair.first)
                .toList().toFlowable().map { albumsLiveData.offer(it) }.map { Unit },
            pair.second
                .toFlowable().map { networkErrorLiveData.offer(it) }.map { Unit }
        )
}