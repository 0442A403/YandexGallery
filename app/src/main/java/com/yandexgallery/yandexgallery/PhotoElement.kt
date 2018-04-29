package com.yandexgallery.yandexgallery

import android.graphics.Bitmap


interface PhotoElement {
    fun setPhoto(bitmap: Bitmap?)
    fun setData(info: PhotoInfo)
}