package com.yandexgallery.yandexgallery

import android.graphics.Bitmap

interface OnGetPhotoListener {
    fun onGetPhoto(bitmap: Bitmap)
}