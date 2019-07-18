package com.github.hadilq.presentationartists

import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import com.github.hadilq.cleanarchitecturefp.domain.usecase.SearchArtists
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`


class ArtistsViewModelTest {

    private lateinit var usecase: SearchArtists
    private lateinit var viewModel: ArtistsViewModel

    @Before
    fun setup() {
        usecase = mock()
        viewModel = ArtistsViewModel(usecase)

        RxAndroidPlugins.reset()
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun reset() {
        RxAndroidPlugins.reset()
    }

    @Test
    fun searchViewActions() {
        val actionsStream = PublishSubject.create<CharSequence>()
        `when`(usecase.findArtists()).doReturn(FlowableTransformer { PublishProcessor.create() })

        viewModel.searchViewActions(actionsStream)

        verify(usecase).findArtists()
    }

    @Test
    fun searchViewActionsEmit() {
        val actionsStream = PublishSubject.create<CharSequence>()
        val artist = Artist("", "", "", "", "", "", "")
        `when`(usecase.findArtists()).doReturn(FlowableTransformer {
            val processor = BehaviorProcessor.create<Pair<Flowable<Artist>, Maybe<Throwable>>>()
            processor.onNext(Pair(Flowable.just(artist), Maybe.empty()))
            processor
        })
        val observer = viewModel.artistsLiveData().test()

        viewModel.searchViewActions(actionsStream)

        verify(usecase).findArtists()

        observer.assertNotComplete()
        observer.assertNoErrors()
        observer.assertValueCount(1)

        assertEquals(1, observer.values()[0].size)
        assertEquals(artist, observer.values()[0][0])
    }
}