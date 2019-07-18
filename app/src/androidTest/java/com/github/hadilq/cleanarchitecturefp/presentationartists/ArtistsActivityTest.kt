package com.github.hadilq.cleanarchitecturefp.presentationartists

import android.content.Context
import android.content.Intent
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.github.hadilq.cleanarchitecturefp.App
import com.github.hadilq.cleanarchitecturefp.R
import com.github.hadilq.cleanarchitecturefp.data.api.Api
import com.github.hadilq.cleanarchitecturefp.data.api.dto.ArtistDto
import com.github.hadilq.cleanarchitecturefp.data.api.dto.EnvelopDto
import com.github.hadilq.cleanarchitecturefp.di.app.AppComponent
import com.github.hadilq.cleanarchitecturefp.di.app.AppModule
import com.github.hadilq.cleanarchitecturefp.di.feature.albumdetails.AlbumDetailsActivityModule
import com.github.hadilq.cleanarchitecturefp.di.feature.albumdetails.AlbumDetailsModule
import com.github.hadilq.cleanarchitecturefp.di.feature.albums.AlbumsActivityModule
import com.github.hadilq.cleanarchitecturefp.di.feature.albums.AlbumsModule
import com.github.hadilq.cleanarchitecturefp.di.feature.artists.ArtistsActivityModule
import com.github.hadilq.cleanarchitecturefp.di.feature.artists.ArtistsModule
import com.github.hadilq.cleanarchitecturefp.di.viewmodel.ViewModelModule
import com.github.hadilq.cleanarchitecturefp.util.ViewActionUtil.waitId
import com.github.hadilq.cleanarchitecturefp.util.ViewActionUtil.withIndex
import com.github.hadilq.cleanarchitecturefp.util.fromJson
import com.github.hadilq.presentationartists.ArtistsActivity
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.squareup.picasso.Picasso
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@RunWith(AndroidJUnit4::class)
class ArtistsActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<ArtistsActivity> = ActivityTestRule(
        ArtistsActivity::class.java,
        true, // initialTouchMode
        false
    )

    private lateinit var app: App
    private val gson = Gson()

    @Inject
    lateinit var api: Api

    @Before
    fun setup() {
        app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as App

        val appComponent = DaggerArtistsActivityTest_TestAppComponent
            .builder()
            .networkModule(TestNetworkModule())
            .application(app)
            .build()
        appComponent.inject(app)
        appComponent.inject(this)
    }

    @Test
    fun launchActivity() {
        val dto: EnvelopDto<ArtistDto> = EnvelopDto(listOf(), null, null)
        `when`(api.searchArtists(any())).doReturn(Single.just(dto))

        activityRule.launchActivity(Intent())

        onView(isRoot()).perform(waitId(R.id.searchIcon, TimeUnit.SECONDS.toMillis(15)))
        onView(isRoot()).perform(waitId(R.id.clearSearchView, TimeUnit.SECONDS.toMillis(15)))
        onView(isRoot()).perform(waitId(R.id.searchView, TimeUnit.SECONDS.toMillis(15)))
    }

    @Test
    fun launchActivityCheckList() {
        val json =
            "{\"data\":[{\"id\":13,\"name\":\"Eminem\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/13\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/13\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/0707267475580b1b82f4da20a1b295c6\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/0707267475580b1b82f4da20a1b295c6\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/0707267475580b1b82f4da20a1b295c6\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/0707267475580b1b82f4da20a1b295c6\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":58,\"nb_fan\":11974051,\"radio\":true,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/13\\/top?limit=50\",\"type\":\"artist\"},{\"id\":1353250,\"name\":\"Eminem feat. Dr. Dre\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/1353250\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/1353250\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":1,\"nb_fan\":8079,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/1353250\\/top?limit=50\",\"type\":\"artist\"},{\"id\":5305180,\"name\":\"Tribute to Eminem\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/5305180\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/5305180\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":2,\"nb_fan\":659,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/5305180\\/top?limit=50\",\"type\":\"artist\"},{\"id\":4288978,\"name\":\"Eminem, Royce\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/4288978\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/4288978\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":898,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/4288978\\/top?limit=50\",\"type\":\"artist\"},{\"id\":1663671,\"name\":\"Dirty Money Music Group featuring Eminem\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/1663671\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/1663671\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":4123,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/1663671\\/top?limit=50\",\"type\":\"artist\"},{\"id\":4205034,\"name\":\"Made famous by TI feat. Eminem\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/4205034\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/4205034\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":439,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/4205034\\/top?limit=50\",\"type\":\"artist\"},{\"id\":1278415,\"name\":\"Bad Meets Evil (Eminem & Royce)\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/1278415\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/1278415\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":630,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/1278415\\/top?limit=50\",\"type\":\"artist\"},{\"id\":1688907,\"name\":\"Obie Trice feat. Eminem\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/1688907\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/1688907\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":471,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/1688907\\/top?limit=50\",\"type\":\"artist\"},{\"id\":10025492,\"name\":\"Black Thought, Mos Def & Eminem\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/10025492\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/10025492\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":286,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/10025492\\/top?limit=50\",\"type\":\"artist\"},{\"id\":490688,\"name\":\"Outsidaz (ft. Eminem)\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/490688\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/490688\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":455,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/490688\\/top?limit=50\",\"type\":\"artist\"},{\"id\":14628647,\"name\":\"Shabaam Sahdeeq, Eminem, Skam2, A.L. Skills, Kwest The Madd Ladd\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/14628647\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/14628647\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":143,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/14628647\\/top?limit=50\",\"type\":\"artist\"},{\"id\":319933,\"name\":\"Eminem, Dr. Dre\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/319933\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/319933\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":1253,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/319933\\/top?limit=50\",\"type\":\"artist\"},{\"id\":4088021,\"name\":\"Nutty Professor The Klumps Soundtrack featuring Redman & eminem\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/4088021\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/4088021\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":330,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/4088021\\/top?limit=50\",\"type\":\"artist\"},{\"id\":4500576,\"name\":\"Bad Meets Evil, Eminem\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/4500576\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/4500576\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":383,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/4500576\\/top?limit=50\",\"type\":\"artist\"},{\"id\":10025506,\"name\":\"Joe Budden, Royce da 5'9\\\", Yelawolf, Joell Ortiz, Eminem & Crooked I\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/10025506\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/10025506\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":547,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/10025506\\/top?limit=50\",\"type\":\"artist\"},{\"id\":10497967,\"name\":\"La Eminemca\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/10497967\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/10497967\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":1,\"nb_fan\":737,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/10497967\\/top?limit=50\",\"type\":\"artist\"},{\"id\":13225405,\"name\":\"Tony Touch and D-12 feat. Eminem\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/13225405\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/13225405\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":132,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/13225405\\/top?limit=50\",\"type\":\"artist\"},{\"id\":14202457,\"name\":\"High & Mighty & Eminem\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/14202457\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/14202457\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":81,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/14202457\\/top?limit=50\",\"type\":\"artist\"},{\"id\":4084536,\"name\":\"Next Friday The Original Motion Picture Soundtrack featuring Eminem\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/4084536\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/4084536\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":462,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/4084536\\/top?limit=50\",\"type\":\"artist\"},{\"id\":1381341,\"name\":\"Eminem feat Dr. Dre Karaoke Band\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/1381341\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/1381341\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":1,\"nb_fan\":1088,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/1381341\\/top?limit=50\",\"type\":\"artist\"},{\"id\":1357423,\"name\":\"Eminem, Featuring Slaughterhouse\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/1357423\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/1357423\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":406,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/1357423\\/top?limit=50\",\"type\":\"artist\"},{\"id\":1616209,\"name\":\"J-black & Masta Ace Eminem\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/1616209\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/1616209\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":0,\"nb_fan\":257,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/1616209\\/top?limit=50\",\"type\":\"artist\"},{\"id\":65218312,\"name\":\"Z\\u00fcleyha Eminem\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/65218312\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/65218312\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":1,\"nb_fan\":7,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/65218312\\/top?limit=50\",\"type\":\"artist\"},{\"id\":369227,\"name\":\"Eminemmylou\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/369227\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/369227\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":3,\"nb_fan\":531,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/369227\\/top?limit=50\",\"type\":\"artist\"},{\"id\":13356931,\"name\":\"Eminemn\",\"link\":\"https:\\/\\/www.deezer.com\\/artist\\/13356931\",\"picture\":\"https:\\/\\/api.deezer.com\\/artist\\/13356931\\/image\",\"picture_small\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/56x56-000000-80-0-0.jpg\",\"picture_medium\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/250x250-000000-80-0-0.jpg\",\"picture_big\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/500x500-000000-80-0-0.jpg\",\"picture_xl\":\"https:\\/\\/e-cdns-images.dzcdn.net\\/images\\/artist\\/\\/1000x1000-000000-80-0-0.jpg\",\"nb_album\":1,\"nb_fan\":127,\"radio\":false,\"tracklist\":\"https:\\/\\/api.deezer.com\\/artist\\/13356931\\/top?limit=50\",\"type\":\"artist\"}],\"total\":48,\"next\":\"https:\\/\\/api.deezer.com\\/search\\/artist?q=eminem&index=25\"}"
        val dto: EnvelopDto<ArtistDto> = gson.fromJson(json)
        `when`(api.searchArtists(any())).doReturn(Single.just(dto))

        activityRule.launchActivity(Intent())

        onView(withId(R.id.searchView)).perform(typeText("Eminem"))
        onView(isRoot()).perform(waitId(R.id.searchIcon, TimeUnit.SECONDS.toMillis(15)))

        onView(withIndex(withId(R.id.nameView), 1)).check(matches(withText("Eminem feat. Dr. Dre")))
    }

    @Singleton
    @Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            TestNetworkModule::class,
            ViewModelModule::class,
            ArtistsModule::class,
            ArtistsActivityModule::class,
            AlbumsModule::class,
            AlbumsActivityModule::class,
            AlbumDetailsModule::class,
            AlbumDetailsActivityModule::class
        ]
    )
    interface TestAppComponent : AppComponent {
        @Component.Builder
        interface Builder {
            @BindsInstance
            fun networkModule(module: TestNetworkModule): Builder

            @BindsInstance
            fun application(app: App): Builder

            fun build(): TestAppComponent
        }

        fun inject(testClass: ArtistsActivityTest)
    }

    @Module
    class TestNetworkModule {

        private val api: Api = mock()

        @Singleton
        @Provides
        fun provideApi(): Api = api

        @Singleton
        @Provides
        fun providePicasso(context: Context): Picasso = Picasso.Builder(context).build()
    }
}