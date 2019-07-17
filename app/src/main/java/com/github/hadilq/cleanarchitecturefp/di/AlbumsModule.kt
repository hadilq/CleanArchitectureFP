package com.github.hadilq.cleanarchitecturefp.di

import com.github.hadilq.cleanarchitecturefp.data.api.Api
import com.github.hadilq.cleanarchitecturefp.data.datasource.AlbumDataSource
import com.github.hadilq.cleanarchitecturefp.data.datasource.impl.AlbumDataSourceImpl
import com.github.hadilq.cleanarchitecturefp.data.repository.AlbumsRepositoryImpl
import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.cleanarchitecturefp.domain.repository.AlbumsRepository
import com.github.hadilq.cleanarchitecturefp.domain.usecase.GetAlbums
import com.github.hadilq.cleanarchitecturefp.domain.usecase.impl.GetAlbumsImpl
import com.github.hadilq.cleanarchitecturefp.domain.util.SchedulerHandler
import dagger.Module
import dagger.Provides
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

@Module
class AlbumsModule {

    @Provides
    fun provideDataSource(api: Api): AlbumDataSource = AlbumDataSourceImpl(api)

    @Provides
    fun provideRepository(dataSource: AlbumDataSource): AlbumsRepository = AlbumsRepositoryImpl(dataSource)

    @Provides
    fun provideSchedulers(): SchedulerHandler<Pair<Flowable<Album>, Maybe<Throwable>>> =
        object : SchedulerHandler<Pair<Flowable<Album>, Maybe<Throwable>>> {
            override fun apply(upstream: Flowable<Pair<Flowable<Album>, Maybe<Throwable>>>): Publisher<Pair<Flowable<Album>, Maybe<Throwable>>> =
                upstream.subscribeOn(Schedulers.io())
        }

    @Provides
    fun provideUsecase(
        repository: AlbumsRepository,
        schedulers: SchedulerHandler<Pair<Flowable<Album>, Maybe<Throwable>>>
    ): GetAlbums = GetAlbumsImpl(repository, schedulers)
}