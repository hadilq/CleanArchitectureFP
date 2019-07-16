package com.github.hadilq.presentationartists

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import com.github.hadilq.presentationcommon.Action
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import javax.inject.Inject

class ArtistsAdapter @Inject constructor(
    private val factory: ArtistViewHolderFactory
) : RecyclerView.Adapter<ArtistViewHolder>() {

    private val list = mutableListOf<Artist>()
    lateinit var actionStream: (Observable<Action>) -> Unit

    fun newList(l: List<Artist>) {
        list.clear()
        list.addAll(l)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        return factory.get(parent, actionStream)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bindTo(list[position])
    }

    class ArtistViewHolderFactory @Inject constructor(
        private val picasso: Picasso
    ) {
        fun get(parent: ViewGroup, actionStream: (Observable<Action>) -> Unit) =
            ArtistViewHolder(parent, actionStream, picasso)
    }
}