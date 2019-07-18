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

    private val artistsLiveData = BehaviorProcessor.create<List<Artist>>()
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