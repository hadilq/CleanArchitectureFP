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
import com.github.hadilq.cleanarchitecturefp.domain.repository.AlbumsRepository
import com.github.hadilq.cleanarchitecturefp.domain.usecase.GetAlbums
import com.github.hadilq.cleanarchitecturefp.domain.util.SwitchFlowableTransformer
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers

class GetAlbumsImpl(
    private val repository: AlbumsRepository
) : GetAlbums {

    override fun albums(): FlowableTransformer<String, Pair<Flowable<Album>, Maybe<Throwable>>> =
        FlowableTransformer { query ->
            query.compose(SwitchFlowableTransformer(repository.fetchAlbums())).subscribeOn(Schedulers.io())
        }

    override fun nextAlbums(): FlowableTransformer<Unit, Pair<Flowable<Album>, Maybe<Throwable>>> =
        FlowableTransformer { query ->
            query.compose(SwitchFlowableTransformer(repository.fetchNextAlbums())).subscribeOn(Schedulers.io())
        }
}