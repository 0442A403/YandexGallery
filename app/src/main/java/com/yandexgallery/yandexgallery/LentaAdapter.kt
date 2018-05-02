package com.yandexgallery.yandexgallery

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.grid_lenta.*
import kotlinx.android.synthetic.main.linear_lenta.*
import android.opengl.ETC1.getHeight



class LentaAdapter(fragmentManager: FragmentManager,
                   private val loader: PhotoLoader,
                   private val onItemCreateListener: OnItemCreateListener,
                   private val onItemClickListener: OnItemClickListener) :
        FragmentPagerAdapter(fragmentManager), OnItemCreateListener, OnItemClickListener{

    val size = 2
    private val gridLentaFragment: GridLentaFragment = GridLentaFragment()
    private val linearLentaFragment: LinearLentaFragment = LinearLentaFragment()
    private var gridLentaAdapter: GridLentaAdapter? = null
    private var linearLentaAdapter: LinearLentaAdapter? = null

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> gridLentaFragment
        else -> linearLentaFragment
    }

    override fun getCount(): Int = size

    override fun getPageTitle(position: Int): CharSequence?
            = if (position == 0)
        "Обзор"
    else
        "Подробно"

    override fun onItemCreate(presenterId: Int, listener: OnBitmapChangeListener, position: Int) {
        onItemCreateListener.onItemCreate(presenterId, listener, position)
    }

    override fun onItemClick(position: Int) {
        onItemClickListener.onItemClick(position)
    }

    fun setPhotoSize(newSize: Int) {
        gridLentaAdapter!!.size = newSize
        linearLentaAdapter!!.size = newSize
    }

    fun setup(context: Context, data: List<PhotoInfo>) {
        setupLinearLenta(context, data)
        setupGridLenta(context)
    }

    private fun setupLinearLenta(context: Context, data: List<PhotoInfo>) {
        val recyclerView = linearLentaFragment.linearLenta
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView!!.canScrollVertically(1))
                    loader.loadPhotos()
            }
        })
        linearLentaAdapter = LinearLentaAdapter(context, 0, this, this, data)
        recyclerView.adapter = linearLentaAdapter
    }

    private fun setupGridLenta(context: Context) {
        val recyclerView = gridLentaFragment.gridLenta
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView!!.canScrollVertically(1))
                    loader.loadPhotos()
            }
        })
        gridLentaAdapter = GridLentaAdapter(context, 0, this, this)
        recyclerView.adapter = gridLentaAdapter
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
            val viewHeight = recyclerView.measuredHeight
        }
    }

    fun hasNotScrollable(): Boolean
            = !gridLentaFragment.notScrollable() || linearLentaFragment.notScrollable()
}