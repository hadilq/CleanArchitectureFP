package com.github.hadilq.presentationalbumdetails

import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.cleanarchitecturefp.domain.entity.Track
import com.github.hadilq.cleanarchitecturefp.domain.usecase.GetAlbumDetails
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`

class AlbumDetailsViewModelTest {

    private lateinit var usecase: GetAlbumDetails
    private lateinit var viewModel: AlbumDetailsViewModel

    @Before
    fun setup() {
        usecase = mock()
        viewModel = AlbumDetailsViewModel(usecase)

        RxAndroidPlugins.reset()
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun reset() {
        RxAndroidPlugins.reset()
    }

    @Test
    fun startLoading() {
        `when`(usecase.details()).doReturn(FlowableTransformer { PublishProcessor.create() })

        viewModel.startLoading("")

        verify(usecase).details()
    }

    @Test
    fun startLoadingEmit() {
        val album = Album("", "", "", "", "", "", "")
        val track = Track("", "", "")
        `when`(usecase.details()).doReturn(FlowableTransformer {
            val processor = BehaviorProcessor.create<Triple<Single<Album>, Flowable<Track>, Maybe<Throwable>>>()
            processor.onNext(Triple(Single.just(album), Flowable.just(track), Maybe.empty()))
            processor
        })
        val albumObserver = viewModel.albumDetailsLiveData().test()
        val trackObserver = viewModel.tracksLiveData().test()

        viewModel.startLoading("")

        verify(usecase).details()

        albumObserver.assertNotComplete()
        albumObserver.assertNoErrors()
        albumObserver.assertValueCount(1)

        assertEquals(album, albumObserver.values()[0])

        trackObserver.assertNotComplete()
        trackObserver.assertNoErrors()
        trackObserver.assertValueCount(2)

        assertEquals(0, trackObserver.values()[0].size)
        assertEquals(1, trackObserver.values()[1].size)
        assertEquals(track, trackObserver.values()[1][0])
    }
}