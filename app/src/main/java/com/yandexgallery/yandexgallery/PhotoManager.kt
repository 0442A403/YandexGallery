package com.yandexgallery.yandexgallery

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.min

class PhotoManager(private val context: Context,
                   private val errorCallback: OnNetworkConnectionErrorListener,
                   private val presenters: List<DynamicPhotoPresenter>,
                   private val onItemClickListener: OnItemClickListener,
                   data: List<PhotoInfo>) : Thread(), PhotoController {
    init {
        for (presenter in presenters)
            presenter.setController(this)
    }
    private val photos: List<Photo> = List(data.size) {
        i -> Photo(data[i].name, data[i].created, data[i].link)
    }
    private var isRunning = true
    private var size = 0
    private var downloaded = 0
    private val photoPack = 9
    private var lastDownloadTime = -1L
    private val betweenDownloadsTime = 350
    override fun run() {
        try {
            while (true) {
                if (downloaded < size) {
                    var index = downloaded
                    val photo = photos[index]
                    if (photo.bitmap == null) {
                        val bitmap = Glide
                                .with(context)
                                .load(photo.link)
                                .asBitmap()
                                .into(-1, -1)
                                .get()
                        photo.bitmap = bitmap
                    }
                    runOnUI {
                        for (callback in photo.callbacks.entries)
                            callback.value.setPhoto(photo.bitmap)
                    }
                    downloaded++
                }
            }
        } catch (e : Exception) {
            runOnUI {
                errorCallback.onNetworkConnectionError()
            }
        }
    }

    override fun setPhotoElement(presenterId: Int, photoElement: PhotoElement, position: Int) {
        var photo = photos[position]
        photoElement.setData(PhotoInfo(photo.date, photo.link, photo.name))
        photos[position].callbacks[presenterId] = photoElement
        photoElement.setPhoto(photos[position].bitmap)
    }

    override fun onItemClick(position: Int) {
        onItemClickListener.onItemClick(position)
    }

    private fun runOnUI(body: () -> Unit) {
        Handler(Looper.getMainLooper()).post(body)
    }

    fun loadNextPack() {
        if (downloaded == size && System.currentTimeMillis() - lastDownloadTime
                >= betweenDownloadsTime) {
            lastDownloadTime = System.currentTimeMillis()
                size = min(photos.size, size + photoPack)
            for (presenter in presenters)
                presenter.setSize(size)
        }
    }

    fun close() {
        isRunning = false
    }
}