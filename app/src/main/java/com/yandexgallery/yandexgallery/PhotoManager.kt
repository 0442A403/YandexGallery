package com.yandexgallery.yandexgallery

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.HashMap

class PhotoManager(private val context: Context) : Thread() {
    private val requests: Queue<Triple<String, Int, OnPhotoDownloadedListener>> = LinkedList()
    private val photos: HashMap<Int, Bitmap> = HashMap()
    private var isRunning = true
    override fun run() {
        super.run()
        while (isRunning) {
            val isNotEmpty = requests.isNotEmpty()
            if (isNotEmpty) {
                var request: Triple<String, Int, OnPhotoDownloadedListener>? = null
                synchronized(requests) {
                    request = requests.poll()
                }
                var defined = false
                synchronized(photos) {
                    defined = photos.containsKey(request!!.second)
                }
                if (defined) {
                    Handler(Looper.getMainLooper()).post {
                        request!!.third.onPhotoDownloaded(photos[request!!.second]!!)
                    }
                    continue
                }
                val bitmap = Glide
                        .with(context)
                        .load(request!!.first)
                        .asBitmap()
                        .into(-1, -1)
                        .get()


                synchronized(photos) {
                    photos[request!!.second] = bitmap
                }
                Handler(Looper.getMainLooper()).post {
                    request!!.third.onPhotoDownloaded(bitmap)
                }
            }
        }
    }

    fun isDone(): Boolean = requests.isEmpty()

    fun setImageForHolder(callback: OnPhotoDownloadedListener, id: Int, link: String) {
        var defined = false
        synchronized(photos) {
            defined = photos.containsKey(id)
        }
        if (defined) {
            synchronized(photos) {
                callback.onPhotoDownloaded(photos[id]!!)
            }
        }
        else {
            callback.onDownloadStarted()
            synchronized(requests) {
                requests.add(Triple(link, id, callback))
            }
        }
    }

    fun close() {
        isRunning = false
    }
}