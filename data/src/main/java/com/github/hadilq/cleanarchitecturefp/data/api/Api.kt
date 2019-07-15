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
package com.github.hadilq.cleanarchitecturefp.data.api

import com.github.hadilq.cleanarchitecturefp.data.api.dto.AlbumDto
import com.github.hadilq.cleanarchitecturefp.data.api.dto.ArtistDto
import com.github.hadilq.cleanarchitecturefp.data.api.dto.EnvelopDto
import com.github.hadilq.cleanarchitecturefp.data.api.dto.TrackDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface Api {

    @GET("search/artist")
    fun searchArtists(@Query("q") query: String): Single<EnvelopDto<ArtistDto>>

    @GET
    fun nextArtists(@Url url: String): Single<EnvelopDto<ArtistDto>>

    @GET("artist/{artistId}/albums")
    fun artistAlbums(@Path("artistId") artistId: String): Single<EnvelopDto<AlbumDto>>

    @GET
    fun nextArtistAlbums(@Url url: String): Single<EnvelopDto<AlbumDto>>

    @GET("album/{albumId}")
    fun album(@Path("albumId") albumId: String): Single<AlbumDto>

    @GET("track/{trackId}")
    fun track(@Path("trackId") trackId: String): Single<TrackDto>
}