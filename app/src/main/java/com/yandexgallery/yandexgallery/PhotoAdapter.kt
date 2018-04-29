package com.yandexgallery.yandexgallery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlin.math.min

class PhotoAdapter(private val context: Context, override val id: Int) :
        RecyclerView.Adapter<PhotoCard>(), DynamicPhotoPresenter {
    private var controller: PhotoController? = null
    private var size = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoCard {
        val layout = LayoutInflater
                .from(context)
                .inflate(R.layout.photo_card, parent, false) as ViewGroup
        return PhotoCard(layout)
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
}