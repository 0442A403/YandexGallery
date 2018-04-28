package com.yandexgallery.yandexgallery

import android.graphics.Bitmap
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.photo_card.view.*

class PhotoCard(private val view: ViewGroup) : RecyclerView.ViewHolder(view),
        OnPhotoDownloadedListener {
    override fun onDownloadStarted() {
        view.progressBarContainer_photoCard.visibility = View.VISIBLE
        view.photo_photoCard.visibility = View.GONE
    }

    override fun onPhotoDownloaded(bitmap: Bitmap) {
        view.progressBarContainer_photoCard.visibility = View.GONE
        view.photo_photoCard.visibility = View.VISIBLE
        view.photo_photoCard.setImageBitmap(bitmap)
    }

    fun setData(created: String, name: String) {
        view.date_photoCard.text = created
        view.name_photoCard.text = name
    }
}