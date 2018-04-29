package com.yandexgallery.yandexgallery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlin.math.min

class PhotoAdapter(private val context: Context,
                   private val info: List<PhotoInfo>,
                   private val errorCallback: OnNetworkConnectionErrorListener) :
        RecyclerView.Adapter<PhotoCard>(),
        OnNetworkConnectionErrorListener,
        OnItemClickListener{
    private var size: Int = 0
    private val photoPack = 10
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
        return PhotoCard(layout, BitmapManager())
    }

    override fun onBindViewHolder(holder: PhotoCard, position: Int) {
        holder.setIsRecyclable(false)
        holder.setData(info[position].created, info[position].name)
        photoManager.setImageForHolder(holder.manager, position, info[position].link)
    }

    override fun getItemCount(): Int = size

    override fun onNetworkConnectionError() {
        errorCallback.onNetworkConnectionError()
    }

    override fun onItemClick(position: Int) {

    }

    fun loadPhotos() {
        if (photoManager.isDone() && System.currentTimeMillis() - lastDownload
                >= betweenDownloadsTime) {
            lastDownload = System.currentTimeMillis()
            val lastSize = size
            size = min(size + photoPack, info.size)
            notifyItemRangeInserted(lastSize, size - lastSize)
        }
    }

    fun close() {
        photoManager.close()
    }
}