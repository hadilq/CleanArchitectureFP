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
package com.github.hadilq.cleanarchitecturefp.domain.usecase

import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe

interface GetAlbums {

    /**
     * Returns a lazy function, where gets artistId as a string
     * then returns a pair of a lazy list of albums and network error.
     */
    fun albums(): FlowableTransformer<String, Pair<Flowable<Album>, Maybe<Throwable>>>

    /**
     * Returns a lazy function, where complete the work of the last call of [albums] method
     * then returns a pair of a lazy list of albums and network error.
     */
    fun nextAlbums(): FlowableTransformer<Unit, Pair<Flowable<Album>, Maybe<Throwable>>>
}