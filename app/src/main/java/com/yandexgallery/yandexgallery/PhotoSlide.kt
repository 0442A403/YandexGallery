package com.yandexgallery.yandexgallery

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.photo_slide.*
import kotlinx.android.synthetic.main.photo_slide.view.*

class PhotoSlide : PhotoElement, Fragment() {
    private var slide: ViewGroup? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        slide = inflater.inflate(R.layout.photo_slide, container, false) as ViewGroup
        return slide
    }

    override fun setPhoto(bitmap: Bitmap?) {
        slide!!.photo_photoSlide.setImageBitmap(bitmap)
    }

    override fun setData(info: PhotoInfo) {
        slide!!
        slide!!.name_photoSlide.text = info.name
    }
}