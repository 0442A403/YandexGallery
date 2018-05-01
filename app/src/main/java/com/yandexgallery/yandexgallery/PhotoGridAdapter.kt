package com.yandexgallery.yandexgallery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class PhotoGridAdapter(private val context: Context, override val id: Int) :
        RecyclerView.Adapter<PhotoGridElement>(), DynamicPhotoPresenter, OnItemClickListener {
    private var controller: PhotoController? = null
    private var size = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoGridElement {
        val layout = LayoutInflater
                .from(context)
                .inflate(R.layout.grid_element, parent, false) as ViewGroup
        return PhotoGridElement(layout, this)
    }

    override fun onBindViewHolder(holder: PhotoGridElement, position: Int) {
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