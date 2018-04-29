package com.yandexgallery.yandexgallery

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.photo_card.view.*

class PhotoCard(view: ViewGroup) : RecyclerView.ViewHolder(view), PhotoElement {
    private val photo = view.photo_photoCard
    private val name = view.name_photoCard
    private val date = view.date_photoCard
    private val progressBarContainer = view.progressBarContainer_photoCard
    override fun setPhoto(bitmap: Bitmap?) {
        if (bitmap != null) {
            progressBarContainer.visibility = View.GONE
            photo.visibility = View.VISIBLE
            photo.setImageBitmap(bitmap)
        }
        else {
            progressBarContainer.visibility = View.VISIBLE
            photo.visibility = View.GONE
        }
    }

    override fun setData(info: PhotoInfo) {
        date.text = info.created
        name.text = info.name
    }
}