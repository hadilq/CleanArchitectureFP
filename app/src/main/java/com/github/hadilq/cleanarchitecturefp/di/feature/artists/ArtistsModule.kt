package com.github.hadilq.cleanarchitecturefp.di.feature.artists

import com.github.hadilq.cleanarchitecturefp.data.api.Api
import com.github.hadilq.cleanarchitecturefp.data.datasource.ArtistDataSource
import com.github.hadilq.cleanarchitecturefp.data.datasource.impl.ArtistDataSourceImpl
import com.github.hadilq.cleanarchitecturefp.data.repository.ArtistsRepositoryImpl
import com.github.hadilq.cleanarchitecturefp.domain.repository.ArtistsRepository
import com.github.hadilq.cleanarchitecturefp.domain.usecase.SearchArtists
import com.github.hadilq.cleanarchitecturefp.domain.usecase.impl.SearchArtistsImpl
import dagger.Module
import dagger.Provides

@Module
class ArtistsModule {

    @Provides
    fun provideDataSource(api: Api): ArtistDataSource = ArtistDataSourceImpl(api)

    @Provides
    fun provideRepository(dataSource: ArtistDataSource): ArtistsRepository = ArtistsRepositoryImpl(dataSource)

    @Provides
    fun provideUsecase(
        repository: ArtistsRepository
    ): SearchArtists = SearchArtistsImpl(repository)
}