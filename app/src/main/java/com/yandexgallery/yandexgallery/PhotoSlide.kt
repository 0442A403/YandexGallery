package com.yandexgallery.yandexgallery

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.photo_slide.*
import kotlinx.android.synthetic.main.photo_slide.view.*

class PhotoSlide(val slide: ViewGroup) : OnBitmapChangeListener {

    private val photoView: PhotoView = slide.photo_photoSlide
    private val progressBar: ProgressBar = slide.progressBar_photoSlide

    override fun onBitmapChange(bitmap: Bitmap?) {
        if (bitmap != null) {
            progressBar.visibility = View.GONE
            photoView.visibility = View.VISIBLE
        }
        else {
            progressBar.visibility = View.VISIBLE
            photoView.visibility = View.GONE
        }
        photoView.setImageBitmap(bitmap)
    }

    fun setData(name: String) {
        slide.name_photoSlide.text = name
    }

    fun clearScaling() {
        photoView.setScale(1f, false)
    }
}