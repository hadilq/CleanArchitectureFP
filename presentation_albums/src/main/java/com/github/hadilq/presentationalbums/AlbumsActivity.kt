package com.github.hadilq.presentationalbums

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.presentationcommon.BaseActivity
import com.github.hadilq.presentationcommon.IntentFactory
import com.github.hadilq.presentationcommon.IntentFactory.AlbumCarrier.Companion.fromAlbum
import kotlinx.android.synthetic.main.albums_activity.*
import javax.inject.Inject

class ArtistsActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var adapter: AlbumsAdapter
    @Inject
    lateinit var intentFactory: IntentFactory

    private lateinit var viewModel: AlbumsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.albums_activity)

        viewModel = viewModel(viewModelFactory) {
            albumsLiveData().observe(::albumsListArrived)
            networkErrorLiveData().observe(::onNetworkError)
            openAlbumDetailsLiveEvent().observe(::onOpenAlbumDetails)
        }

        setupRecyclerView()

        viewModel.startLoading(intent.getStringExtra(IntentFactory.BundleKey.ALBUMS_ARTIST_ID.KEY))
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

    private fun setupRecyclerView() {
        albumsView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        albumsView.adapter = adapter
        adapter.actionStream = {
            viewModel.recyclerViewActions(it)
        }
    }
}