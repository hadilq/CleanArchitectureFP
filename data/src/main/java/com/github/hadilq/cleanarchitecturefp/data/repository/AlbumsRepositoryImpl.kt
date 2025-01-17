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

import android.util.Log
import com.github.hadilq.cleanarchitecturefp.data.datasource.AlbumDataSource
import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.cleanarchitecturefp.domain.repository.AlbumsRepository
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.processors.PublishProcessor

class AlbumsRepositoryImpl(
    private val dataSource: AlbumDataSource
) : AlbumsRepository {

    private val networkErrorsProcessor = PublishProcessor.create<Throwable>()

    override fun fetchAlbum(): FlowableTransformer<String, Pair<Single<Album>, Maybe<Throwable>>> =
        FlowableTransformer {
            Flowable.just(it.compose(dataSource.fetchAlbum()))
                .map { f ->
                    Pair(
                        f.compose(handleError()).firstOrError(),
                        networkErrorsProcessor.firstElement()
                    )
                }
                .compose(handleError())
        }

    override fun fetchAlbums(): FlowableTransformer<String, Pair<Flowable<Album>, Maybe<Throwable>>> =
        fetchWith(dataSource.fetchAlbums())

    override fun fetchNextAlbums(): FlowableTransformer<Unit, Pair<Flowable<Album>, Maybe<Throwable>>> =
        fetchWith(dataSource.fetchNextAlbums())

    private fun <T> fetchWith(transformer: FlowableTransformer<T, Album>): FlowableTransformer<T, Pair<Flowable<Album>, Maybe<Throwable>>> =
        FlowableTransformer {
            Flowable.just(it.compose(transformer))
                .map { f ->
                    Pair(
                        f.compose(handleError()),
                        networkErrorsProcessor.firstElement()
                    )
                }
                .compose(handleError())
        }

    private fun <U> handleError(): FlowableTransformer<U, U> =
        FlowableTransformer {
            it.onErrorResumeNext { e: Throwable ->
                networkErrorsProcessor.offer(e)
                Log.e("ArtistsRepositoryImpl", "An error happened", e)
                Flowable.empty<U>()
            }
        }
}