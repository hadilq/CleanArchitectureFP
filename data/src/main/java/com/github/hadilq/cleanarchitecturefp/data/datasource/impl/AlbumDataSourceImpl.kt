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
package com.github.hadilq.cleanarchitecturefp.data.datasource.impl

import com.github.hadilq.cleanarchitecturefp.data.api.Api
import com.github.hadilq.cleanarchitecturefp.data.api.dto.AlbumDto
import com.github.hadilq.cleanarchitecturefp.data.api.dto.EnvelopDto
import com.github.hadilq.cleanarchitecturefp.data.datasource.AlbumDataSource
import com.github.hadilq.cleanarchitecturefp.data.datasource.map
import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

class AlbumDataSourceImpl(
    private val api: Api
) : AlbumDataSource {

    private var artist: Artist? = null
    private var next: String? = null

    override fun fetchAlbum(): FlowableTransformer<String, Album> =
        FlowableTransformer {
            it.flatMap { id ->
                api.album(id)
                    .toFlowable()
                    .map { ad -> ad.map() }
            }
        }

    override fun fetchAlbums(): FlowableTransformer<Artist, Album> =
        FlowableTransformer {
            it.flatMap { artist ->
                api.artistAlbums(artist.id)
                    .toFlowable()
                    .compose(convert(artist))
            }
        }

    override fun fetchNextAlbums(): FlowableTransformer<Unit, Album> =
        FlowableTransformer {
            it.flatMap {
                next?.let { n ->
                    artist?.let { a ->
                        api.nextArtistAlbums(n)
                            .toFlowable()
                            .compose(convert(a))
                    } ?: run { Flowable.empty<Album>() }
                } ?: run {
                    artist?.let { a ->
                        // This is happened when the first page gets error and we retry the request
                        Flowable.just(a).compose(fetchAlbums())
                    } ?: run { Flowable.empty<Album>() }
                }
            }
        }

    private fun convert(a: Artist): FlowableTransformer<EnvelopDto<AlbumDto>, Album> =
        FlowableTransformer {
            it.map { envelop ->
                next = envelop.next
                next?.let { artist = a } ?: apply { artist = null }
                envelop.data
            }
                .flatMap { list ->
                    Flowable.fromIterable(list)
                }
                .map { ad -> ad.map() }
        }
}