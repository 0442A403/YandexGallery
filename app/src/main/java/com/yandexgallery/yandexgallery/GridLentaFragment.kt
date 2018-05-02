package com.yandexgallery.yandexgallery

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class GridLentaFragment : Fragment() {
    private var layout: RecyclerView? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.grid_lenta, container, false) as RecyclerView
        return layout
    }

    fun notScrollable(): Boolean = !layout!!.isVerticalScrollBarEnabled
}