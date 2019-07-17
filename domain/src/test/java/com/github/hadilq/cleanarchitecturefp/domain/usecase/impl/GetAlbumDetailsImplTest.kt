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
import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import com.github.hadilq.cleanarchitecturefp.domain.entity.Track
import com.github.hadilq.cleanarchitecturefp.domain.repository.AlbumsRepository
import com.github.hadilq.cleanarchitecturefp.domain.repository.TracksRepository
import com.github.hadilq.cleanarchitecturefp.domain.usecase.GetAlbumDetails
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`

class GetAlbumDetailsImplTest {

    private lateinit var albumsRepository: AlbumsRepository
    private lateinit var trackRepository: TracksRepository
    private lateinit var usecase: GetAlbumDetails

    @Before
    fun setup() {
        albumsRepository = mock()
        trackRepository = mock()
        usecase =
            GetAlbumDetailsImpl(albumsRepository, trackRepository)

        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun reset() {
        RxJavaPlugins.reset()
    }

    @Test
    fun fetchTrack() {
        //Given
        val artist = Artist("", "Test", "", "", "", "", "")
        val album = Album("", "Test", "", "", "", "", "", artist)
        val track = Track("", "Test", "", album, artist, emptyList())
        `when`(trackRepository.fetchTrack()).doReturn(FlowableTransformer {
            Flowable.just(
                Pair(
                    Flowable.just(track),
                    Maybe.empty()
                )
            )
        })

        //When
        Flowable.just("").compose(usecase.track()).test()

        // Then
        verify(trackRepository).fetchTrack()
    }

    @Test
    fun fetchTrackDoubleCall() {
        //Given
        val artist = Artist("", "Test", "", "", "", "", "")
        val album = Album("", "Test", "", "", "", "", "", artist)
        val track = Track("", "Test", "", album, artist, emptyList())
        val flowableFactory: () -> Flowable<Pair<Flowable<Track>, Maybe<Throwable>>> = mock()
        `when`(trackRepository.fetchTrack()).doReturn(
            FlowableTransformer {
                flowableFactory()
            }
        )
        `when`(flowableFactory.invoke()).doReturn(Flowable.just(Pair(Flowable.just(track), Maybe.empty())))

        //When
        Flowable.fromArray("", "").compose(usecase.track()).test()

        // Then
        verify(trackRepository).fetchTrack()
        verify(flowableFactory, times(2)).invoke()
    }
}