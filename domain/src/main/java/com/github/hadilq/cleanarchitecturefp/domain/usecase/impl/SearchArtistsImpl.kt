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

import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import com.github.hadilq.cleanarchitecturefp.domain.repository.ArtistsRepository
import com.github.hadilq.cleanarchitecturefp.domain.usecase.SearchArtists
import com.github.hadilq.cleanarchitecturefp.domain.util.SwitchFlowableTransformer
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers

class SearchArtistsImpl(
    private val repository: ArtistsRepository
) : SearchArtists {

    override fun findArtists(): FlowableTransformer<String, Pair<Flowable<Artist>, Maybe<Throwable>>> =
        fetch(repository.fetchArtists())

    override fun findNextArtists(): FlowableTransformer<Unit, Pair<Flowable<Artist>, Maybe<Throwable>>> =
        fetch(repository.fetchNextArtists())

    private fun <T> fetch(f: FlowableTransformer<T, Pair<Flowable<Artist>, Maybe<Throwable>>>): FlowableTransformer<T, Pair<Flowable<Artist>, Maybe<Throwable>>> =
        FlowableTransformer { u ->
            u.observeOn(Schedulers.io()).compose(SwitchFlowableTransformer(f))
        }
}