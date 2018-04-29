package com.yandexgallery.yandexgallery

import android.graphics.Bitmap

interface OnPhotoDownloadedListener {
    fun onPhotoDownloaded(bitmap: Bitmap)
}
