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
package com.github.hadilq.cleanarchitecturefp.data.repository

import com.github.hadilq.cleanarchitecturefp.data.datasource.ArtistDataSource
import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import com.github.hadilq.cleanarchitecturefp.domain.repository.ArtistsRepository
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.processors.PublishProcessor

class ArtistsRepositoryImpl(
    private val dataSource: ArtistDataSource
) : ArtistsRepository {

    private val networkErrorsProcessor = PublishProcessor.create<Throwable>()

    override fun fetchArtists(): FlowableTransformer<String, Pair<Flowable<Artist>, Maybe<Throwable>>> =
        fetchWith(dataSource.fetchArtists())

    override fun fetchNextArtists(): FlowableTransformer<Unit, Pair<Flowable<Artist>, Maybe<Throwable>>> =
        fetchWith(dataSource.fetchNextArtists())

    private fun <T> fetchWith(transformer: FlowableTransformer<T, Artist>): FlowableTransformer<T, Pair<Flowable<Artist>, Maybe<Throwable>>> =
        FlowableTransformer {
            Flowable.just(it.compose(transformer))
                .map { f ->
                    Pair(
                        f.onErrorResumeNext { e: Throwable -> networkErrorsProcessor.offer(e); Flowable.empty() },
                        networkErrorsProcessor.firstElement()
                    )
                }
                .onErrorResumeNext { e: Throwable -> networkErrorsProcessor.offer(e); Flowable.empty() }
        }
}