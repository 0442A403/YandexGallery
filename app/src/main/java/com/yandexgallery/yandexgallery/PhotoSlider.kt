package com.yandexgallery.yandexgallery

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View

class PhotoSlider(override val id: Int,
                  fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager), DynamicPhotoPresenter {

    private var controller: PhotoController? = null
    private var size = 0
    private val slides: ArrayList<PhotoSlide> = ArrayList()

    override fun getCount(): Int
            = size

    override fun isViewFromObject(view: View, `object`: Any): Boolean
            = view == `object`

    override fun setSize(newSize: Int) {
        val oldSize = size
        size = newSize
        for (i in oldSize until size) {
            val slide = PhotoSlide()
            slides.add(slide)
            controller!!.setPhotoElement(id, slide, i)
        }
        notifyDataSetChanged()
    }

    override fun setController(controller: PhotoController) {
        if (this.controller == null)
            this.controller = controller
    }

    override fun getItem(position: Int): Fragment {
        return slides[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "$position из $size"
    }
}