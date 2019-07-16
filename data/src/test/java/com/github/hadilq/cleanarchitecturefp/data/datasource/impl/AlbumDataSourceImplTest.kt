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
import com.github.hadilq.cleanarchitecturefp.data.util.FileReader
import com.github.hadilq.cleanarchitecturefp.data.util.fromJson
import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`

class AlbumDataSourceImplTest {

    private lateinit var api: Api
    private val gson = Gson()
    private val fileReader = FileReader()

    @Before
    fun setup() {
        api = mock()
    }

    @Test
    fun fetchAlbums() {
        val json = fileReader.readString("artist-albums.json")
        val dto: EnvelopDto<AlbumDto> = gson.fromJson(json)
        `when`(api.artistAlbums("T")).doReturn(Single.just(dto))

        val test = api.artistAlbums("T").test()

        test.assertComplete()
        test.assertNoErrors()
        test.assertValueCount(1)

        Assert.assertEquals(62L, test.values()[0].total)
        Assert.assertEquals(25, test.values()[0].data.size)
    }

    @Test
    fun fetchAlbumsFromDataSource() {
        val json = fileReader.readString("artist-albums.json")
        val dto: EnvelopDto<AlbumDto> = gson.fromJson(json)
        `when`(api.artistAlbums("T")).doReturn(Single.just(dto))
        val dataSource = AlbumDataSourceImpl(api)
        val artist = Artist("T", "", "", "", "", "", "")

        val test = Flowable.just(artist.id).compose(dataSource.fetchAlbums()).test()

        test.assertComplete()
        test.assertNoErrors()
        test.assertValueCount(25)

        Assert.assertEquals("Originals", test.values()[0].title)
    }
}