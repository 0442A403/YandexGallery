package com.yandexgallery.yandexgallery

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
        NetworkActivity(), OnGetPhotoInfoListener, OnNetworkConnectionErrorListener,
        OnItemClickListener, OnItemCreateListener, OnSizeChangeListener, PhotoLoader {

    private var infoGetter: PhotoInfoGetter? = null
    private var photoManager: PhotoManager? = null
    private var sliderEnable = false
    private var slideAdapter: PhotoSlideAdapter? = null
    private var lentaAdapter: LentaAdapter? = null
//    private var autoLoaderWorking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = getSharedPreferences("AppData", Context.MODE_PRIVATE)
        val token = data.getString("Token", "")

        enableLenta()
        setUpRefreshLayout(token)
        setUpViewPager(viewPager_mainActivity)
        getPhotoInfo(token)
    }

    override fun onItemClick(position: Int) {
        Log.i("MyYandex", "$position")
        enableSlider(position)
    }

    override fun onBackPressed() {
        if (!sliderEnable)
            super.onBackPressed()
        else {
            slideAdapter!!.clearScaling()
            enableLenta()
        }
    }

    override fun onDestroy() {
        close()
        super.onDestroy()
    }

    override fun onNetworkConnectionError() {
        noticeAboutBadNetwork()
    }

    private fun setSliderTitle(position: Int) {
        sliderToolbar_mainActivity.title = photoSlider_mainActivity.adapter!!.getPageTitle(position)
    }

    private fun getPhotoInfo(token: String) {
        if (hasConnection()) {
            infoGetter = PhotoInfoGetter(token, this, this)
            infoGetter!!.start()
        }
        else {
            disableRefreshing()
            noticeAboutBadNetwork()
        }
    }

    private fun disableRefreshing() {
        refreshLayout_mainActivity!!.isRefreshing = false
    }

    private fun setUpRefreshLayout(token: String) {
        refreshLayout_mainActivity.setOnRefreshListener {
            close()
            getPhotoInfo(token)
        }
    }

    override fun onGetPhotoInfo(result: List<PhotoInfo>) {
        disableRefreshing()
        setUpAdapters(result, photoSlider_mainActivity)
        setUpPhotoManager(result.map { i -> i.link })
        loadPhotos()
    }

    override fun loadPhotos() {
        if (hasConnection())
            photoManager?.loadNext()
        else
            noticeAboutBadNetwork()
    }

    private fun setUpAdapters(data: List<PhotoInfo>, slider: ViewPager) {
        lentaAdapter!!.setup(this, data)
        val sliderAdapter = setUpSlider(slider, data.map { i -> i.name })
    }

    private fun setUpViewPager(viewPager: ViewPager) {
        lentaAdapter = LentaAdapter(supportFragmentManager, this, this, this)
        viewPager.adapter = lentaAdapter
        tabLayout_mainActivity.setupWithViewPager(viewPager)
    }

    private fun setUpPhotoManager(data: List<String>) {
        photoManager = PhotoManager(applicationContext, this, this, data)
        photoManager!!.start()
    }

    private fun setUpSlider(slider: ViewPager, names: List<String>) {
        slideAdapter = PhotoSlideAdapter(
                1,
                LayoutInflater.from(this),
                photoSlider_mainActivity,
                names,
                this
        )
        slider.adapter = slideAdapter
//        slider.setPageTransformer(true, SlideAnimation())
        slider.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                if (!photoSlider_mainActivity.canScrollHorizontally(1))
                    loadPhotos()
            }

            override fun onPageSelected(position: Int) {
                slideAdapter!!.clearScaling()
                setSliderTitle(position)
            }

            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int) {}
        })
    }

    private fun enableSlider(position: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        setSupportActionBar(sliderToolbar_mainActivity)
        setSliderTitle(position)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        photoSlider_mainActivity.setCurrentItem(position, false)
        photoSlider_mainActivity.adapter!!.notifyDataSetChanged()

        slider_mode.visibility = View.VISIBLE
        lenta_mode.visibility = View.GONE

        sliderEnable = true
    }

    private fun enableLenta() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.statusBarColor = ContextCompat.getColor(this, R.color.yandexYellow)

        slider_mode.visibility = View.GONE
        lenta_mode.visibility = View.VISIBLE

        sliderEnable = false

        setSupportActionBar(lentaToolbar_mainActivity)
        val toggle = ActionBarDrawerToggle(
                this,
                lenta_mode,
                lentaToolbar_mainActivity,
                R.string.app_name,
                R.string.app_name)
        lenta_mode.setDrawerListener(toggle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (sliderEnable)
            enableLenta()
        return true
    }

    override fun onItemCreate(presenterId: Int, listener: OnBitmapChangeListener, position: Int) {
        photoManager?.addOnBitmapChangeListener(presenterId, listener, position)
    }

    override fun onSizeChange(newSize: Int) {
        lentaAdapter?.setPhotoSize(newSize)
        slideAdapter?.size = newSize
    }

    private fun close() {
        photoManager?.close()
        photoManager = null
        infoGetter?.close()
        infoGetter = null
    }
}