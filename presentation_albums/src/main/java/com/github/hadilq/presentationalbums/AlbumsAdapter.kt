package com.github.hadilq.presentationalbums

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.presentationcommon.Action
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import javax.inject.Inject

class AlbumsAdapter(
    private val factory: AlbumViewHolderFactory,
    private val width: Int,
    private val actionStream: (Observable<Action>) -> Unit
) : RecyclerView.Adapter<AlbumViewHolder>() {

    private val list = mutableListOf<Album>()

    fun newList(l: List<Album>) {
        list.clear()
        list.addAll(l)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return factory.get(parent, actionStream, width)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bindTo(list[position])
    }

    class AlbumAdapterFactory @Inject constructor(
        private val factory: AlbumViewHolderFactory
    ) {
        fun get(width: Int, actionStream: (Observable<Action>) -> Unit) =
            AlbumsAdapter(factory, width, actionStream)
    }

    class AlbumViewHolderFactory @Inject constructor(
        private val picasso: Picasso
    ) {
        fun get(parent: ViewGroup, actionStream: (Observable<Action>) -> Unit, width: Int) =
            AlbumViewHolder(parent, actionStream, picasso, width)
    }
}