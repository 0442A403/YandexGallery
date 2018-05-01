package com.yandexgallery.yandexgallery

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class PhotoSlider(override val id: Int,
                  private val inflater: LayoutInflater,
                  private val pager: ViewPager) :
        PagerAdapter(), DynamicPhotoPresenter {

    private var controller: PhotoController? = null
    private var size = 0
    private val slides: ArrayList<PhotoSlide> = ArrayList()

    override fun getCount(): Int
            = size

    override fun isViewFromObject(view: View, `object`: Any): Boolean
            = view == `object`

    override fun getItemPosition(`object`: Any): Int {
        val index = slides.indexOf(`object`)
        return if (index == -1)
            POSITION_NONE
        else
            index
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = slides[position].slide
        container.addView(view)
        return view
    }

    override fun setSize(newSize: Int) {
        val oldSize = size
        size = newSize
        for (i in oldSize until size) {
            val slideView = inflater.inflate(R.layout.photo_slide, pager, false) as ViewGroup
            val slide = PhotoSlide(slideView)
            slides.add(slide)
            controller!!.setPhotoElement(id, slide, i)
        }
        notifyDataSetChanged()
    }

    override fun setController(controller: PhotoController) {
        if (this.controller == null)
            this.controller = controller
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "${position + 1} из $size"
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(slides[position].slide)
    }
}