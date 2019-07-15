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
import com.github.hadilq.cleanarchitecturefp.domain.repository.TracksRepository
import com.github.hadilq.cleanarchitecturefp.domain.usecase.GetTracks
import com.github.hadilq.cleanarchitecturefp.domain.util.SchedulerHandler
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
import org.reactivestreams.Publisher

class GetTrackImplTest {

    private lateinit var repository: TracksRepository
    private lateinit var usecase: GetTracks

    @Before
    fun setup() {
        repository = mock()
        usecase = GetTracksImpl(repository, object : SchedulerHandler<Pair<String, Album>> {

            override fun apply(upstream: Flowable<Pair<String, Album>>): Publisher<Pair<String, Album>> {
                return upstream
            }
        })

        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun reset() {
        RxJavaPlugins.reset()
    }

    @Test
    fun findArtists() {
        //Given
        val artist = Artist("", "Test", "", "", "", "", "")
        val album = Album("", "Test", "", "", "", "", "", artist)
        val track = Track("", "Test", "", album, artist, emptyList())
        `when`(repository.fetchTracks()).doReturn(FlowableTransformer {
            Flowable.just(
                Pair(
                    Flowable.just(track),
                    Maybe.empty()
                )
            )
        })

        //When
        Flowable.just(Pair("", album)).compose(usecase.tracks()).test()

        // Then
        verify(repository).fetchTracks()
    }

    @Test
    fun findArtistsDoubleCall() {
        //Given
        val artist = Artist("", "Test", "", "", "", "", "")
        val album = Album("", "Test", "", "", "", "", "", artist)
        val track = Track("", "Test", "", album, artist, emptyList())
        val flowableFactory: () -> Flowable<Pair<Flowable<Track>, Maybe<Throwable>>> = mock()
        `when`(repository.fetchTracks()).doReturn(
            FlowableTransformer {
                flowableFactory()
            }
        )
        `when`(flowableFactory.invoke()).doReturn(Flowable.just(Pair(Flowable.just(track), Maybe.empty())))

        //When
        Flowable.fromArray(Pair("", album), Pair("", album)).compose(usecase.tracks()).test()

        // Then
        verify(repository).fetchTracks()
        verify(flowableFactory, times(2)).invoke()
    }
}