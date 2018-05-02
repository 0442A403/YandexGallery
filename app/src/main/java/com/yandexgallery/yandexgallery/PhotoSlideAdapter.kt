package com.yandexgallery.yandexgallery

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.chrisbanes.photoview.PhotoView

class PhotoSlideAdapter(override val id: Int,
                        private val inflater: LayoutInflater,
                        private val pager: ViewPager,
                        private val names: List<String>,
                        private val onItemCreateListener: OnItemCreateListener) :
        PagerAdapter(), PhotoPresenter {

    private val slides: ArrayList<PhotoSlide> = ArrayList()
    var size = 0
        set(value) {
            val oldSize = size
            field = value
            for (i in oldSize until value) {
                val slideView = inflater.inflate(R.layout.photo_slide, pager, false) as ViewGroup
                val slide = PhotoSlide(slideView)
                slide.setData(names[i])
                onItemCreateListener.onItemCreate(id, slide, i)
                slides.add(slide)
            }
            notifyDataSetChanged()
        }



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
//        onItemCreateListener.onItemCreate(id, slides[position], position)
        container.addView(view)
        return view
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "${position + 1} из $size"
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(slides[position].slide)
    }

    fun clearScaling() {
//        for (slide in slides)
//            slide.clearScaling()
    }
}