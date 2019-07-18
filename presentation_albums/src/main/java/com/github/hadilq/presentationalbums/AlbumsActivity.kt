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
package com.github.hadilq.presentationalbums

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.presentationcommon.BaseActivity
import com.github.hadilq.presentationcommon.IntentFactory
import com.github.hadilq.presentationcommon.IntentFactory.AlbumCarrier.Companion.fromAlbum
import kotlinx.android.synthetic.main.albums_activity.*
import javax.inject.Inject


class AlbumsActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var adapterFactory: AlbumsAdapter.AlbumAdapterFactory
    @Inject
    lateinit var intentFactory: IntentFactory

    private lateinit var viewModel: AlbumsViewModel
    private lateinit var adapter: AlbumsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.albums_activity)

        viewModel = viewModel(viewModelFactory) {
            albumsLiveData().observe(::albumsListArrived)
            networkErrorLiveData().observe(::onNetworkError)
            openAlbumDetailsLiveEvent().observe(::onOpenAlbumDetails)
        }

        setupBackButton()
        setupRecyclerView()

        viewModel.startLoading(intent.getStringExtra(IntentFactory.BundleKey.ALBUMS_ARTIST_ID.KEY))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun albumsListArrived(albums: List<Album>) {
        adapter.newList(albums)
    }

    private fun onNetworkError(@Suppress("UNUSED_PARAMETER") throwable: Throwable) {
        showFailure(albumsLayout, R.string.network_error, viewModel::retry)
    }

    private fun onOpenAlbumDetails(album: Album) {
        val intent = intentFactory.create(IntentFactory.Page.ALBUM_DETAILS)
        intent.putExtra(IntentFactory.BundleKey.ALBUM_DETAILS_ALBUM.KEY, album.fromAlbum())
        startActivity(intent)
    }

    private fun setupBackButton() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        adapter = adapterFactory.get(displayMetrics.widthPixels / 2) {
            viewModel.recyclerViewActions(it)
        }
        albumsView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        albumsView.adapter = adapter
        albumsView.addItemDecoration(SpacingItemDecoration(resources.getDimension(R.dimen.standard_half).toInt()))
    }
}