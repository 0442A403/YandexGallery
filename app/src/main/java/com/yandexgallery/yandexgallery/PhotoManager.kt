package com.yandexgallery.yandexgallery

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import kotlin.math.min

class PhotoManager(private val context: Context,
                   private val errorCallback: OnNetworkConnectionErrorListener,
                   private val onSizeChangeListener: OnSizeChangeListener,
                   private val links: List<String>) : Thread() {

    private val photoDatas: List<PhotoData> = List(links.size) { i -> PhotoData(links[i]) }
    private var isRunning = true
    private var size = 0
    private var downloaded = 0
    private val photoPack = 12
    private var lastDownloadTime = -1L
    private val betweenDownloadsTime = 350

    override fun run() {
        try {
            while (isRunning && downloaded != links.size) {
                if (downloaded < size) {
                    val data = photoDatas[downloaded]
                    if (data.bitmap == null) {
                        val bitmap = Glide
                                .with(context)
                                .load(data.link)
                                .asBitmap()
                                .into(-1, -1)
                                .get()
                        data.bitmap = bitmap
                    }
                    Handler(Looper.getMainLooper()).post {
                        for (callback in data.listeners.entries)
                            callback.value.onBitmapChange(data.bitmap)
                    }
                    downloaded++
                }
            }
        } catch (e : Exception) {
            Handler(Looper.getMainLooper()).post {
                errorCallback.onNetworkConnectionError()
            }
        }
    }

    fun loadNext() {
        if (downloaded == size && System.currentTimeMillis() - lastDownloadTime
                >= betweenDownloadsTime) {
            lastDownloadTime = System.currentTimeMillis()
            val newSize = min(photoDatas.size, size + photoPack)
            onSizeChangeListener.onSizeChange(newSize)
            size = newSize
        }
    }

    fun close() {
        isRunning = false
    }

    fun addOnBitmapChangeListener(presenterId: Int, listener: OnBitmapChangeListener, position: Int) {
        photoDatas[position].listeners[presenterId] = listener
        listener.onBitmapChange(photoDatas[position].bitmap)
    }

    private class PhotoData(val link: String) {
        val listeners: HashMap<Int, OnBitmapChangeListener> = HashMap()
        var bitmap: Bitmap? = null
    }
}