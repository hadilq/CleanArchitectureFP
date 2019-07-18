package com.github.hadilq.presentationartists

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import com.github.hadilq.presentationcommon.BaseActivity
import com.github.hadilq.presentationcommon.IntentFactory
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.artists_activity.*
import javax.inject.Inject

class ArtistsActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var adapterFactory: ArtistsAdapter.ArtistsAdapterFactory
    @Inject
    lateinit var intentFactory: IntentFactory

    private lateinit var viewModel: ArtistsViewModel
    private lateinit var adapter: ArtistsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artists_activity)

        viewModel = viewModel(viewModelFactory) {
            artistsLiveData().observe(::artistsListArrived)
            networkErrorLiveData().observe(::onNetworkError)
            openAlbumsLiveEvent().observe(::onOpenAlbums)
        }

        setupSearchView()
        setupRecyclerView()
    }

    private fun artistsListArrived(artists: List<Artist>) {
        adapter.newList(artists)
    }

    private fun onNetworkError(@Suppress("UNUSED_PARAMETER") throwable: Throwable) {
        showFailure(artistsLayout, R.string.network_error, viewModel::retry)
    }

    private fun onOpenAlbums(artistId: String) {
        val intent = intentFactory.create(IntentFactory.Page.ALBUMS)
        intent.putExtra(IntentFactory.BundleKey.ALBUMS_ARTIST_ID.KEY, artistId)
        startActivity(intent)
    }

    private fun setupRecyclerView() {
        adapter = adapterFactory.get {
            viewModel.recyclerViewActions(it)
        }
        artistsView.layoutManager = LinearLayoutManager(this)
        artistsView.adapter = adapter
    }

    private fun setupSearchView() {
        viewModel.searchViewActions(RxTextView.textChanges(searchView))
        RxView.clicks(clearSearchView).map { searchView.text?.clear() }.subscribe().track()
    }
}