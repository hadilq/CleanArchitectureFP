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
package com.github.hadilq.cleanarchitecturefp.data.datasource

import com.github.hadilq.cleanarchitecturefp.data.api.dto.AlbumDto
import com.github.hadilq.cleanarchitecturefp.data.api.dto.ArtistDto
import com.github.hadilq.cleanarchitecturefp.data.api.dto.TrackDto
import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import com.github.hadilq.cleanarchitecturefp.domain.entity.Track

fun ArtistDto.map(): Artist = Artist(
    id = id,
    name = name,
    picture = picture,
    pictureSmall = pictureSmall,
    pictureMedium = pictureMedium,
    pictureBig = pictureBig,
    pictureXl = pictureXl
)

fun AlbumDto.map(): Album = Album(
    id = id,
    title = title,
    cover = cover,
    coverSmall = coverSmall,
    coverMedium = coverMedium,
    coverBig = coverBig,
    coverXl = coverXl,
    artist = artist?.map(),
    tracks = tracks?.data?.map(TrackDto::map)
)

fun TrackDto.map(): Track = Track(
    id = id,
    title = title,
    titleShort = titleShort,
    album = album?.map(),
    contributors = contributors?.map { it.map() }
)