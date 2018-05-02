package com.yandexgallery.yandexgallery

interface OnItemCreateListener {
    fun onItemCreate(presenterId: Int, listener: OnBitmapChangeListener, position: Int)
}