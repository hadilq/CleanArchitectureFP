package com.github.hadilq.presentationalbumdetails

import android.os.Bundle
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

        setupRecyclerView()
        setupAlbum()
    }

    private fun albumArrived(album: Album) {
        adapter.removeAll(BaseViewData.ViewType.TITLE)
        adapter.addAt(0, TitleViewData(album))
        adapter.notifyDataSetChanged()

        coverView.loadFromUrl(picasso, album.coverBig)
    }

    private fun tracksArrived(tracks: List<Track>) {
        adapter.removeAll(BaseViewData.ViewType.TRACK)
        adapter.addAll(tracks.map { TrackViewData(it) })
        adapter.notifyDataSetChanged()
    }

    private fun onNetworkError(@Suppress("UNUSED_PARAMETER") throwable: Throwable) {
        showFailure(albumDetailsLayout, R.string.network_error, viewModel::retry)
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