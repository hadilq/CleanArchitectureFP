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
import com.github.hadilq.cleanarchitecturefp.data.datasource.TrackDataSource
import com.github.hadilq.cleanarchitecturefp.data.datasource.map
import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.cleanarchitecturefp.domain.entity.Track
import io.reactivex.FlowableTransformer

class TrackDataSourceImpl(
    private val api: Api
) : TrackDataSource {

    override fun fetchTrack(): FlowableTransformer<Pair<String, Album>, Track> =
        FlowableTransformer {
            it.flatMap { p ->
                api.track(p.first)
                    .toFlowable()
                    .map { ad -> ad.map(p.second) }
            }
        }
}