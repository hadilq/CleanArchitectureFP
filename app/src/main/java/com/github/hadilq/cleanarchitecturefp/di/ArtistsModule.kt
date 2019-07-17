package com.github.hadilq.cleanarchitecturefp.di

import com.github.hadilq.cleanarchitecturefp.data.api.Api
import com.github.hadilq.cleanarchitecturefp.data.datasource.ArtistDataSource
import com.github.hadilq.cleanarchitecturefp.data.datasource.impl.ArtistDataSourceImpl
import com.github.hadilq.cleanarchitecturefp.data.repository.ArtistsRepositoryImpl
import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import com.github.hadilq.cleanarchitecturefp.domain.repository.ArtistsRepository
import com.github.hadilq.cleanarchitecturefp.domain.usecase.SearchArtists
import com.github.hadilq.cleanarchitecturefp.domain.usecase.impl.SearchArtistsImpl
import com.github.hadilq.cleanarchitecturefp.domain.util.SchedulerHandler
import dagger.Module
import dagger.Provides
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

@Module
class ArtistsModule {

    @Provides
    fun provideDataSource(api: Api): ArtistDataSource = ArtistDataSourceImpl(api)

    @Provides
    fun provideRepository(dataSource: ArtistDataSource):ArtistsRepository = ArtistsRepositoryImpl(dataSource)

    @Provides
    fun provideSchedulers(): SchedulerHandler<Pair<Flowable<Artist>, Maybe<Throwable>>> =
        object : SchedulerHandler<Pair<Flowable<Artist>, Maybe<Throwable>>> {

            override fun apply(upstream: Flowable<Pair<Flowable<Artist>, Maybe<Throwable>>>): Publisher<Pair<Flowable<Artist>, Maybe<Throwable>>> =
                upstream.subscribeOn(Schedulers.io())
        }

    @Provides
    fun provideUsecase(
        repository: ArtistsRepository,
        schedulers: SchedulerHandler<Pair<Flowable<Artist>, Maybe<Throwable>>>
    ): SearchArtists = SearchArtistsImpl(repository, schedulers)
}