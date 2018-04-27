package com.yandexgallery.yandexgallery

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Handler
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.HashMap

class PhotoManager(private val context: Context,
                   private val handler: Handler) : AsyncTask<Void, Void, Void>() {
    private val requests: Queue<Triple<String, Int, OnPhotoDownloadedListener>> = LinkedList()
    private val photos: HashMap<Int, Bitmap> = HashMap()
    override fun doInBackground(vararg params: Void?): Void? {
        while (true) {
            var isNotEmpty = false
            synchronized(this) {
                isNotEmpty = requests.isNotEmpty()
            }
            if (isNotEmpty) {
                var request: Triple<String, Int, OnPhotoDownloadedListener>? = null
                synchronized(this) {
                    request = requests.poll()
                }
                if (photos.containsKey(request!!.second)) {
                    handler.post {
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
                photos[request!!.second] = bitmap
                handler.post {
                    request!!.third.onPhotoDownloaded(bitmap)
                }
            }

        }
    }

    fun isDone(): Boolean = requests.isEmpty()

    fun setImageForHolder(callback: OnPhotoDownloadedListener, id: Int, link: String) {
        synchronized(this) {
            requests.add(Triple(link, id, callback))
        }
    }
}