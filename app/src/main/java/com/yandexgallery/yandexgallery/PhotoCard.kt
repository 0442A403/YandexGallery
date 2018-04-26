package com.yandexgallery.yandexgallery

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.photo_card.view.*

class PhotoCard(private val view: ViewGroup) : RecyclerView.ViewHolder(view), OnGetPhotoListener {

    override fun onGetPhoto(bitmap: Bitmap)
            = view.photo_photoCard.setImageBitmap(bitmap)

    fun setData(data: PhotoInfo) {
        view.date_photoCard.text = data.created
        view.name_photoCard.text = data.name
    }
}