package com.yandexgallery.yandexgallery

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlin.math.min

class PhotoAdapter(private val context: Context,
                   private val info: List<PhotoInfo>,
                   private val errorCallback: OnNetworkConnectionErrorListener) :
        RecyclerView.Adapter<PhotoCard>(), OnNetworkConnectionErrorListener{
    private var size: Int = 0
    private val photoPack = 10
    private val holders = ArrayList<PhotoCard>()
    private var lastDownload = -1L
    private val photoManager = PhotoManager(context, this)
    private val betweenDownloadsTime = 250
    init {
        photoManager.start()
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
        holder.setIsRecyclable(false)
        holder.setData(info[holder.adapterPosition].created, info[holder.adapterPosition].name)
        photoManager.setImageForHolder(holder,
                holder.adapterPosition,
                info[holder.adapterPosition].link)
    }

    override fun getItemCount(): Int = size

    override fun onNetworkConnectionError() {
        errorCallback.onNetworkConnectionError()
    }

    fun loadPhotos() {
        if (photoManager.isDone() && System.currentTimeMillis() - lastDownload
                >= betweenDownloadsTime) {
            lastDownload = System.currentTimeMillis()
            size = min(size + photoPack, info.size)
            notifyDataSetChanged()
        }
    }

    fun close() {
        photoManager.close()
    }
}