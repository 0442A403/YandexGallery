package com.yandexgallery.yandexgallery

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlin.math.min

class PhotoAdapter(private val context: Context,
                   private val info: List<PhotoInfo>,
                   handler: Handler) : RecyclerView.Adapter<PhotoCard>() {
    private var size: Int = 0
    private val photoPack = 6
    private val holders = ArrayList<PhotoCard>()
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
        holder.setData(info[position])
        photoManager.addToQueue(info[position].link, holder)
    }

    override fun getItemCount(): Int = size

    fun loadPhotos() {
        if (photoManager.isDone()) {
            val oldSize = size
            size = min(size + photoPack, info.size)
            notifyItemRangeInserted(oldSize, size - oldSize)
        }
    }
}