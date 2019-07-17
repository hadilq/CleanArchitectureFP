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
package com.github.hadilq.cleanarchitecturefp.domain.usecase.impl

import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.cleanarchitecturefp.domain.entity.Track
import com.github.hadilq.cleanarchitecturefp.domain.repository.AlbumsRepository
import com.github.hadilq.cleanarchitecturefp.domain.repository.TracksRepository
import com.github.hadilq.cleanarchitecturefp.domain.usecase.GetAlbumDetails
import com.github.hadilq.cleanarchitecturefp.domain.util.SchedulerHandler
import com.github.hadilq.cleanarchitecturefp.domain.util.SwitchFlowableTransformer
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.Single

class GetAlbumDetailsImpl(
    private val albumsRepository: AlbumsRepository,
    private val tracksRepository: TracksRepository,
    private val schedulers: SchedulerHandler<String>
) : GetAlbumDetails {

    override fun details(): FlowableTransformer<String, Triple<Single<Album>, Flowable<Track>, Maybe<Throwable>>> =
        FlowableTransformer { query ->
            query
                .compose(schedulers)
                .flatMap { albumId ->
                    Flowable.just(albumId)
                        .compose(albumsRepository.fetchAlbum())
                        .map {
                            val album = it.first.blockingGet()
                            val pair = completeLoadingTracks(album)
                            Triple(Single.just(album), pair.first, it.second.ambWith(pair.second))
                        }
                }
        }

    private fun completeLoadingTracks(album: Album): Pair<Flowable<Track>, Maybe<Throwable>> =
        Flowable.fromIterable(album.tracks)
            .map { it.id }
            .compose(track())
            .toList()
            .map { list ->
                val iterable = Flowable.fromIterable(list).share()

                Pair(
                    iterable.flatMap { it.first },
                    iterable.map { it.second }.toList().map { Maybe.amb(it) }.blockingGet()
                )
            }.blockingGet()

    override fun track(): FlowableTransformer<String, Pair<Flowable<Track>, Maybe<Throwable>>> =
        FlowableTransformer { query ->
            query.compose(schedulers).compose(SwitchFlowableTransformer(tracksRepository.fetchTrack()))
        }
}