package com.yandexgallery.yandexgallery

import android.graphics.Bitmap

data class Photo(val name: String,
                 val date: String,
                 val link: String,
                 var bitmap: Bitmap? = null) {
    val callbacks: HashMap<Int, PhotoElement> = HashMap()
}