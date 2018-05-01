package com.yandexgallery.yandexgallery

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.grid_element.view.*
import kotlinx.android.synthetic.main.photo_card.view.*

class PhotoGridElement(view: ViewGroup, private val onItemClickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(view), PhotoElement {

    private val photo = view.photo_gridElement
    private val progressBarContainer = view.progressBar_gridElement

    init {
        photo.setOnClickListener {
            onItemClickListener.onItemClick(adapterPosition)
        }
        progressBarContainer.setOnClickListener {
            onItemClickListener.onItemClick(adapterPosition)
        }
    }

    override fun setPhoto(bitmap: Bitmap?) {
        if (bitmap != null) {
            progressBarContainer.visibility = View.GONE
            photo.visibility = View.VISIBLE
        }
        else {
            progressBarContainer.visibility = View.VISIBLE
            photo.visibility = View.GONE
        }
        photo.setImageBitmap(bitmap)
    }

    override fun setData(info: PhotoInfo) { }
}