package com.yandexgallery.yandexgallery

import android.content.Context
import android.media.Image
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class PhotoAdapter(private val context: Context): BaseAdapter() {
    private val elements = ArrayList<ImageView>()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View
            = elements[position]

    override fun getItem(position: Int) = elements[position]

    override fun getItemId(position: Int): Long = elements[position].hashCode().toLong()

    override fun getCount(): Int = elements.size

    fun addElement(view: ImageView, url: String) {
        Glide.with(context).load(url).into(view)
        elements.add(view)
    }
}