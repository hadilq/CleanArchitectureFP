package com.github.hadilq.presentationalbums

import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.cleanarchitecturefp.domain.usecase.GetAlbums
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
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`

class AlbumsViewModelTest {

    private lateinit var usecase: GetAlbums
    private lateinit var viewModel: AlbumsViewModel

    @Before
    fun setup() {
        usecase = mock()
        viewModel = AlbumsViewModel(usecase)

        RxAndroidPlugins.reset()
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun reset() {
        RxAndroidPlugins.reset()
    }

    @Test
    fun startLoading() {
        `when`(usecase.albums()).doReturn(FlowableTransformer { PublishProcessor.create() })

        viewModel.startLoading("")

        verify(usecase).albums()
    }

    @Test
    fun startLoadingEmit() {
        val album = Album("", "", "", "", "", "", "")
        `when`(usecase.albums()).doReturn(FlowableTransformer {
            val processor = BehaviorProcessor.create<Pair<Flowable<Album>, Maybe<Throwable>>>()
            processor.onNext(Pair(Flowable.just(album), Maybe.empty()))
            processor
        })
        val observer = viewModel.albumsLiveData().test()

        viewModel.startLoading("")

        verify(usecase).albums()

        observer.assertNotComplete()
        observer.assertNoErrors()
        observer.assertValueCount(2)

        assertEquals(0, observer.values()[0].size)
        assertEquals(1, observer.values()[1].size)
        assertEquals(album, observer.values()[1][0])
    }
}