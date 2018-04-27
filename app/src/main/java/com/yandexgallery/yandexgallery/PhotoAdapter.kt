package com.yandexgallery.yandexgallery

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlin.math.min

class PhotoAdapter(private val context: Context,
                   private val info: List<PhotoInfo>,
                   handler: Handler) : RecyclerView.Adapter<PhotoCard>() {
    private var size: Int = 0
    private val photoPack = 10
    private val holders = ArrayList<PhotoCard>()
    private var lastDownload = -1L
    private val photoManager = PhotoManager(context, handler)

    init {
        photoManager.execute()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoCard {
        val layout = LayoutInflater
                .from(context)
                .inflate(R.layout.photo_card, parent, false) as ViewGroup
        val card = PhotoCard(layout)
        synchronized(this) {
            holders.add(card)
        }
        return card
    }

    override fun onBindViewHolder(holder: PhotoCard, position: Int) {
        holder.setData(info[position].created, info[position].name)
        photoManager.setImageForHolder(holder, position, info[position].link)
    }

    override fun getItemCount(): Int = size

    fun loadPhotos() {
        if (photoManager.isDone() && System.currentTimeMillis() - lastDownload >= 700) {
            lastDownload = System.currentTimeMillis()
            val oldSize = size
            size = min(size + photoPack, info.size)
            notifyItemRangeChanged(oldSize, size - oldSize)
        }
    }

    fun close() {
        photoManager.cancel(false)
    }
}