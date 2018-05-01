package com.yandexgallery.yandexgallery

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.grid_lenta.view.*
import kotlinx.android.synthetic.main.lenta_layout.*

class LentaAdapter(fragmentManager: FragmentManager,
                   private val loader: PhotoLoader) : FragmentPagerAdapter(fragmentManager) {

    private val size = 2
    private var gridLenta: GridLenta? = null
    private var recyclerLenta: RecyclerLenta? = null

    init {
        gridLenta = GridLenta()
        recyclerLenta = RecyclerLenta()
    }

    override fun getItem(position: Int): Fragment
            = when (position) {
        0 -> {
            gridLenta = GridLenta()
            gridLenta!!
        }
        else ->  {
            recyclerLenta = RecyclerLenta()
            recyclerLenta!!
        }
    }

    override fun getCount(): Int = size

    override fun getPageTitle(position: Int): CharSequence?
            = if (position == 0)
        "Обзор"
    else
        "Подробно"

    fun setUpRecyclerView(context: Context) : DynamicPhotoPresenter {
        val recyclerView = recyclerLenta!!.recyclerLenta
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView!!.canScrollVertically(1))
                    loader.loadPhotos()
            }
        })
        val cardAdapter = PhotoCardAdapter(context, 0)
        recyclerView.adapter = cardAdapter
        return cardAdapter
    }

    fun setUpGridView(context: Context): DynamicPhotoPresenter {
        val recyclerView = gridLenta!!.recyclerLenta
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView!!.canScrollVertically(1))
                    loader.loadPhotos()
            }
        })
        val gridAdapter = PhotoGridAdapter(context, 0)
        recyclerView.adapter = gridAdapter
        return gridAdapter
    }

    fun hasNotScrollable(): Boolean = !gridLenta!!.recyclerLenta.isVerticalScrollBarEnabled ||
            !recyclerLenta!!.recyclerLenta.isVerticalScrollBarEnabled
}