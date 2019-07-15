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
import com.github.hadilq.cleanarchitecturefp.data.api.dto.ArtistDto
import com.github.hadilq.cleanarchitecturefp.data.api.dto.EnvelopDto
import com.github.hadilq.cleanarchitecturefp.data.datasource.ArtistDataSource
import com.github.hadilq.cleanarchitecturefp.data.datasource.map
import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

class ArtistDataSourceImpl(
    private val api: Api
) : ArtistDataSource {

    private var query: String? = null
    private var next: String? = null

    override fun fetchArtists(): FlowableTransformer<String, Artist> =
        FlowableTransformer {
            it.flatMap { q ->
                query = q
                api.searchArtists(q)
                    .toFlowable()
            }.compose(convert())
        }

    override fun fetchNextArtists(): FlowableTransformer<Unit, Artist> =
        FlowableTransformer {
            it.flatMap {
                next?.let { n ->
                    api.nextArtists(n)
                        .toFlowable()
                        .compose(convert())
                } ?: run {
                    query?.let { q ->
                        // This is happened when the first page gets error and we retry the request
                        Flowable.just(q).compose(fetchArtists())
                    } ?: run { Flowable.empty<Artist>() }
                }
            }
        }

    private fun convert(): FlowableTransformer<EnvelopDto<ArtistDto>, Artist> =
        FlowableTransformer {
            it.map { envelop ->
                next = envelop.next
                next ?: apply { query = null }
                envelop.data
            }
                .flatMap { list ->
                    Flowable.fromIterable(list)
                }
                .map(ArtistDto::map)
        }
}