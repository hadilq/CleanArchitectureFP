package com.github.hadilq.cleanarchitecturefp.di.feature.albumdetails

import com.github.hadilq.cleanarchitecturefp.data.api.Api
import com.github.hadilq.cleanarchitecturefp.data.datasource.TrackDataSource
import com.github.hadilq.cleanarchitecturefp.data.datasource.impl.TrackDataSourceImpl
import com.github.hadilq.cleanarchitecturefp.data.repository.TracksRepositoryImpl
import com.github.hadilq.cleanarchitecturefp.domain.repository.AlbumsRepository
import com.github.hadilq.cleanarchitecturefp.domain.repository.TracksRepository
import com.github.hadilq.cleanarchitecturefp.domain.usecase.GetAlbumDetails
import com.github.hadilq.cleanarchitecturefp.domain.usecase.impl.GetAlbumDetailsImpl
import dagger.Module
import dagger.Provides

@Module
class AlbumDetailsModule {

    @Provides
    fun provideDataSource(api: Api): TrackDataSource = TrackDataSourceImpl(api)

    @Provides
    fun provideRepository(dataSource: TrackDataSource): TracksRepository = TracksRepositoryImpl(dataSource)

    @Provides
    fun provideUsecase(
        albumsRepository: AlbumsRepository,
        trackRepository: TracksRepository
    ): GetAlbumDetails = GetAlbumDetailsImpl(albumsRepository, trackRepository)
}