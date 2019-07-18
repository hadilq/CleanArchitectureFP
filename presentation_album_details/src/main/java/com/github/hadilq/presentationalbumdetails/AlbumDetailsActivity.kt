/***
 * Copyright 2019 Hadi Lashkari Ghouchani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * */
package com.github.hadilq.presentationalbumdetails

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.cleanarchitecturefp.domain.entity.Track
import com.github.hadilq.presentationalbumdetails.recycler.AlbumDetailsAdapter
import com.github.hadilq.presentationalbumdetails.recycler.data.BaseViewData
import com.github.hadilq.presentationalbumdetails.recycler.data.TitleViewData
import com.github.hadilq.presentationalbumdetails.recycler.data.TrackViewData
import com.github.hadilq.presentationcommon.BaseActivity
import com.github.hadilq.presentationcommon.IntentFactory
import com.github.hadilq.presentationcommon.loadFromUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.album_details_activity.*
import javax.inject.Inject

class AlbumDetailsActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var adapter: AlbumDetailsAdapter
    @Inject
    lateinit var picasso: Picasso

    private lateinit var viewModel: AlbumDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.album_details_activity)

        viewModel = viewModel(viewModelFactory) {
            albumDetailsLiveData().observe(::albumArrived)
            tracksLiveData().observe(::tracksArrived)
            networkErrorLiveData().observe(::onNetworkError)
        }

        setupBackButton()
        setupRecyclerView()
        setupAlbum()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun albumArrived(album: Album) {
        adapter.removeAll(BaseViewData.ViewType.TITLE)
        adapter.addAt(0, TitleViewData(album))
        adapter.notifyDataSetChanged()

        coverView.loadFromUrl(picasso, album.coverBig)
    }

    private fun tracksArrived(tracks: List<Track>) {
        adapter.removeAll(BaseViewData.ViewType.TRACK)
        adapter.addAll(tracks.mapIndexed { index: Int, track: Track -> TrackViewData(track, index + 1) })
        adapter.notifyDataSetChanged()
    }

    private fun onNetworkError(@Suppress("UNUSED_PARAMETER") throwable: Throwable) {
        showFailure(albumDetailsLayout, R.string.network_error, viewModel::retry)
    }

    private fun setupBackButton() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
        collapsingToolbarLayout.title = null

    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupAlbum() {
        val album = intent.getParcelableExtra<IntentFactory.AlbumCarrier>(
            IntentFactory.BundleKey.ALBUM_DETAILS_ALBUM.KEY
        ).toAlbum()
        adapter.add(TitleViewData(album))
        adapter.notifyItemInserted(adapter.itemCount - 1)

        coverView.loadFromUrl(picasso, album.coverBig)

        viewModel.startLoading(album.id)
    }
}