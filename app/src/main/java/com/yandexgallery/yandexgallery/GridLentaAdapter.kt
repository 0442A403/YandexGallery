package com.yandexgallery.yandexgallery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class GridLentaAdapter(private val context: Context,
                       override val id: Int,
                       private val onItemClickListener: OnItemClickListener,
                       private val onItemCreateListener: OnItemCreateListener) :
        RecyclerView.Adapter<PhotoGridElement>(), OnItemClickListener, PhotoPresenter {

    var size = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoGridElement {
        val layout = LayoutInflater
                .from(context)
                .inflate(R.layout.grid_element, parent, false) as ViewGroup
        return PhotoGridElement(layout, this)
    }

    override fun onBindViewHolder(holder: PhotoGridElement, position: Int) {
        holder.setIsRecyclable(false)
        onItemCreateListener.onItemCreate(id, holder, position)
    }

    override fun getItemCount(): Int = size

    override fun onItemClick(position: Int) {
        onItemClickListener.onItemClick(position)
    }
}