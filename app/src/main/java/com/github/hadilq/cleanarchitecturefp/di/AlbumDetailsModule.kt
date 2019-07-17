package com.github.hadilq.cleanarchitecturefp.di

import com.github.hadilq.cleanarchitecturefp.data.api.Api
import com.github.hadilq.cleanarchitecturefp.data.datasource.TrackDataSource
import com.github.hadilq.cleanarchitecturefp.data.datasource.impl.TrackDataSourceImpl
import com.github.hadilq.cleanarchitecturefp.data.repository.TracksRepositoryImpl
import com.github.hadilq.cleanarchitecturefp.domain.repository.AlbumsRepository
import com.github.hadilq.cleanarchitecturefp.domain.repository.TracksRepository
import com.github.hadilq.cleanarchitecturefp.domain.usecase.GetAlbumDetails
import com.github.hadilq.cleanarchitecturefp.domain.usecase.impl.GetAlbumDetailsImpl
import com.github.hadilq.cleanarchitecturefp.domain.util.SchedulerHandler
import dagger.Module
import dagger.Provides
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

@Module
class AlbumDetailsModule {

    @Provides
    fun provideDataSource(api: Api): TrackDataSource = TrackDataSourceImpl(api)

    @Provides
    fun provideRepository(dataSource: TrackDataSource): TracksRepository = TracksRepositoryImpl(dataSource)

    @Provides
    fun provideSchedulers(): SchedulerHandler<String> =
        object : SchedulerHandler<String> {
            override fun apply(upstream: Flowable<String>): Publisher<String> =
                upstream.subscribeOn(Schedulers.io())
        }

    @Provides
    fun provideUsecase(
        albumsRepository: AlbumsRepository,
        trackRepository: TracksRepository,
        schedulers: SchedulerHandler<String>
    ): GetAlbumDetails = GetAlbumDetailsImpl(albumsRepository, trackRepository, schedulers)
}