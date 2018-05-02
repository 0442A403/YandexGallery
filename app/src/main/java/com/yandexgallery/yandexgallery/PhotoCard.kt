package com.yandexgallery.yandexgallery

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.photo_card.view.*

class PhotoCard(view: ViewGroup,
                private val onItemClickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(view), OnBitmapChangeListener {

    private val photo = view.photo_photoCard
    private val name = view.name_photoCard
    private val date = view.date_photoCard
    private val progressBarContainer = view.progressBarContainer_photoCard

    init {
        photo.setOnClickListener {
            onItemClickListener.onItemClick(adapterPosition)
        }
        progressBarContainer.setOnClickListener {
            onItemClickListener.onItemClick(adapterPosition)
        }
    }

    override fun onBitmapChange(bitmap: Bitmap?) {
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

    fun setData(date: String, name: String) {
        this.date.text = date
        this.name.text = name
    }
}