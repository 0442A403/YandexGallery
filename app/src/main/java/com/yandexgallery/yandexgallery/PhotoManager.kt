package com.yandexgallery.yandexgallery

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Handler
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList

class PhotoManager(private val context: Context,
                   private val handler: Handler) : AsyncTask<Void, Void, Void>() {
    private val requests: Queue<Pair<String, OnGetPhotoListener>> = LinkedList()
    private val photos: ArrayList<Bitmap> = ArrayList()
    override fun doInBackground(vararg params: Void?): Void? {
        while (true) {
            var isNotEmpty = false
            synchronized(this) {
                isNotEmpty = requests.isNotEmpty()
            }
            if (isNotEmpty) {
                var request: Pair<String, OnGetPhotoListener>? = null
                synchronized(this) {
                    request = requests.poll()
                }
                val bitmap = Glide
                        .with(context)
                        .load(request!!.first)
                        .asBitmap()
                        .into(-1, -1)
                        .get()
                photos.add(bitmap)
                handler.post {
                    request!!.second.onGetPhoto(bitmap)
                }
            }

        }
    }

    fun addToQueue(link: String, callback: OnGetPhotoListener) {
        synchronized(this) {
            requests.add(Pair(link, callback))
        }
    }

    fun isDone(): Boolean = requests.isEmpty()
}