package com.yandexgallery.yandexgallery

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.photo_slide.*
import kotlinx.android.synthetic.main.photo_slide.view.*

class PhotoSlide(val slide: ViewGroup) : PhotoElement {

    private var photoView: PhotoView = slide.photo_photoSlide
    private var bitmap: Bitmap? = null

    init {
        photoView.minimumScale = 1f
        photoView.mediumScale = 1.01f
    }

    override fun setPhoto(bitmap: Bitmap?) {
        this.bitmap = bitmap
        setupView()
    }

    override fun setData(info: PhotoInfo) {
        slide.name_photoSlide.text = info.name
    }

    fun setPhotoView(photoView: PhotoView) {
        slide.removeViewAt(0)
        slide.addView(photoView, 0)
        this.photoView = photoView
        photoView.minimumScale = 1f
        photoView.mediumScale = 1.01f
        setupView()
    }

    private fun setupView() {
        if (bitmap != null) {
            slide.progressBar_photoSlide.visibility = View.GONE
            photoView.visibility = View.VISIBLE
        }
        else {
            slide.progressBar_photoSlide.visibility = View.VISIBLE
            photoView.visibility = View.GONE
        }
        photoView.setImageBitmap(bitmap)
    }

    fun clearScaling() {
        photoView.setScale(1f, false)
    }
}