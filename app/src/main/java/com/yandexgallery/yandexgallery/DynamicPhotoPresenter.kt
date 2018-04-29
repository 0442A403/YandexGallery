package com.yandexgallery.yandexgallery

interface DynamicPhotoPresenter {
    val id: Int
    fun setSize(newSize: Int)
    fun setController(controller: PhotoController)
}
