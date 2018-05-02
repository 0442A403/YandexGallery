package com.yandexgallery.yandexgallery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.photo_card.view.*
import kotlin.math.min

class LinearLentaAdapter(private val context: Context,
                         override val id: Int,
                         private val onItemClickListener: OnItemClickListener,
                         private val onItemCreateListener: OnItemCreateListener,
                         private val data: List<PhotoInfo>) :
        RecyclerView.Adapter<PhotoCard>(), PhotoPresenter, OnItemClickListener {

    var size = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoCard {
        val layout = LayoutInflater
                .from(context)
                .inflate(R.layout.photo_card, parent, false) as ViewGroup
        return PhotoCard(layout, this)
    }

    override fun onBindViewHolder(holder: PhotoCard, position: Int) {
        holder.setIsRecyclable(false)
        holder.setData(data[position].created, data[position].name)
        onItemCreateListener.onItemCreate(id, holder, position)
    }

    override fun getItemCount(): Int = size

    override fun onItemClick(position: Int) {
        onItemClickListener.onItemClick(position)
    }
}