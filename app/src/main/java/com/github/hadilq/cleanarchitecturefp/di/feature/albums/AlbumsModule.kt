package com.github.hadilq.cleanarchitecturefp.di.feature.albums

import com.github.hadilq.cleanarchitecturefp.data.api.Api
import com.github.hadilq.cleanarchitecturefp.data.datasource.AlbumDataSource
import com.github.hadilq.cleanarchitecturefp.data.datasource.impl.AlbumDataSourceImpl
import com.github.hadilq.cleanarchitecturefp.data.repository.AlbumsRepositoryImpl
import com.github.hadilq.cleanarchitecturefp.domain.repository.AlbumsRepository
import com.github.hadilq.cleanarchitecturefp.domain.usecase.GetAlbums
import com.github.hadilq.cleanarchitecturefp.domain.usecase.impl.GetAlbumsImpl
import dagger.Module
import dagger.Provides

@Module
class AlbumsModule {

    @Provides
    fun provideDataSource(api: Api): AlbumDataSource = AlbumDataSourceImpl(api)

    @Provides
    fun provideRepository(dataSource: AlbumDataSource): AlbumsRepository = AlbumsRepositoryImpl(dataSource)

    @Provides
    fun provideUsecase(
        repository: AlbumsRepository
    ): GetAlbums = GetAlbumsImpl(repository)
}