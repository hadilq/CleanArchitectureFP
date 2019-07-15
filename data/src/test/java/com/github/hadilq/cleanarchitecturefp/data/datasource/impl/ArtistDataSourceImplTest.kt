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
import com.github.hadilq.cleanarchitecturefp.data.util.FileReader
import com.github.hadilq.cleanarchitecturefp.data.util.fromJson
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`

class ArtistDataSourceImplTest {

    private lateinit var api: Api
    private val gson = Gson()
    private val fileReader = FileReader()

    @Before
    fun setup() {
        api = mock()
    }

    @Test
    fun searchArtists() {
        val json = fileReader.readString("search-artist.json")
        val dto: EnvelopDto<ArtistDto> = gson.fromJson(json)
        `when`(api.searchArtists("T")).doReturn(Single.just(dto))

        val test = api.searchArtists("T").test()

        test.assertComplete()
        test.assertNoErrors()
        test.assertValueCount(1)

        Assert.assertEquals(48L, test.values()[0].total)
        Assert.assertEquals(25, test.values()[0].data.size)
    }

    @Test
    fun searchArtistsFromDataSource() {
        val json = fileReader.readString("search-artist.json")
        val dto: EnvelopDto<ArtistDto> = gson.fromJson(json)
        `when`(api.searchArtists("T")).doReturn(Single.just(dto))
        val dataSource = ArtistDataSourceImpl(api)

        val test = Flowable.just("T").compose(dataSource.fetchArtists()).test()

        test.assertComplete()
        test.assertNoErrors()
        test.assertValueCount(25)

        Assert.assertEquals("Eminem", test.values()[0].name)
    }


}