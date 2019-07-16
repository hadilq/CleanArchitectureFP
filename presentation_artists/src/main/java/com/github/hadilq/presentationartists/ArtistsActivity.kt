package com.github.hadilq.presentationartists

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import com.github.hadilq.presentationcommon.BaseActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.artists_activity.*
import javax.inject.Inject

class ArtistsActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var adapter: ArtistsAdapter

    private lateinit var viewModel: ArtistsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artists_activity)

        viewModel = viewModel(viewModelFactory) {
            artistsLiveData().observe(::artistsListArrived)
            networkErrorLiveData().observe(::onNetworkError)
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

    private fun setupRecyclerView() {
        artistsView.layoutManager = LinearLayoutManager(this)
        artistsView.adapter = adapter
        adapter.actionStream = {
            viewModel.recyclerViewActions(it)
        }
    }

    private fun setupSearchView() {
        viewModel.searchViewActions(RxTextView.textChanges(searchView))
    }
}