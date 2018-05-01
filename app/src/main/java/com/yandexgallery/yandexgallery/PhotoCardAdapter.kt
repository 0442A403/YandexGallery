package com.yandexgallery.yandexgallery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.photo_card.view.*
import kotlin.math.min

class PhotoCardAdapter(private val context: Context, override val id: Int) :
        RecyclerView.Adapter<PhotoCard>(), DynamicPhotoPresenter, OnItemClickListener {
    private var controller: PhotoController? = null
    private var size = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoCard {
        val layout = LayoutInflater
                .from(context)
                .inflate(R.layout.photo_card, parent, false) as ViewGroup
        return PhotoCard(layout, this)
    }

    override fun onBindViewHolder(holder: PhotoCard, position: Int) {
        holder.setIsRecyclable(false)
        controller!!.setPhotoElement(id, holder, position)
    }

    override fun getItemCount(): Int = size

    override fun setSize(newSize: Int) {
        size = newSize
        notifyDataSetChanged()
    }

    override fun setController(controller: PhotoController) {
        if (this.controller == null)
            this.controller = controller
    }

    override fun onItemClick(position: Int) {
        controller?.onItemClick(position)
    }
}