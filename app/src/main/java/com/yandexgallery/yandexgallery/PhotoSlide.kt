package com.yandexgallery.yandexgallery

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.photo_slide.*
import kotlinx.android.synthetic.main.photo_slide.view.*

class PhotoSlide(val slide: ViewGroup) : PhotoElement {
    override fun setPhoto(bitmap: Bitmap?) {
        if (bitmap != null) {
            slide.progressBar_photoSlide.visibility = View.GONE
            slide.photo_photoSlide.visibility = View.VISIBLE
        }
        else {
            slide.progressBar_photoSlide.visibility = View.VISIBLE
            slide.photo_photoSlide.visibility = View.GONE
        }
        slide.photo_photoSlide.setImageBitmap(bitmap)
    }

    override fun setData(info: PhotoInfo) {
        slide.name_photoSlide.text = info.name
    }
}