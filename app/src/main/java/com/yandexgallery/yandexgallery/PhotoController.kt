package com.yandexgallery.yandexgallery

interface PhotoController : OnItemClickListener {
    fun setPhotoElement(presenterId: Int, photoElement: PhotoElement, position: Int)
}
