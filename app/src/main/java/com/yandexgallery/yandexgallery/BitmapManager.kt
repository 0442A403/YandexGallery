package com.yandexgallery.yandexgallery

import android.graphics.Bitmap

class BitmapManager : OnPhotoDownloadedListener {
    private val callbacks = ArrayList<SetPhotoListener>()
    fun addCallback(callback: SetPhotoListener) {
        callbacks.add(callback)
    }
    override fun onPhotoDownloaded(bitmap: Bitmap) {
        for (callback in callbacks)
            callback.setPhoto(bitmap)
    }
}