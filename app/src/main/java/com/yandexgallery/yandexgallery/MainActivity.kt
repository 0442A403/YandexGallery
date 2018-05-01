package com.yandexgallery.yandexgallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
        NetworkActivity(), OnGetPhotoInfoListener,
        OnNetworkConnectionErrorListener, OnItemClickListener {

    private var infoGetter: PhotoInfoGetter? = null
    private var photoManager: PhotoManager? = null
    private var sliderEnable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = getSharedPreferences("AppData", Context.MODE_PRIVATE)
        val token = data.getString("Token", "")

        setSupportActionBar(lentaToolbar_mainActivity)
        val toggle = ActionBarDrawerToggle(
                this,
                drawerLayout_mainActivity,
                lentaToolbar_mainActivity,
                R.string.app_name,
                R.string.app_name)
        drawerLayout_mainActivity.addDrawerListener(toggle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
        navigationView_mainActivity.setNavigationItemSelectedListener {
            close()
            data.edit().remove("Token").apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            true
        }

        recyclerView_mainActivity.layoutManager = LinearLayoutManager(this)
        recyclerView_mainActivity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView!!.canScrollVertically(1))
                    loadPhotos()
            }
        })
        photoSlider_mainActivity.setPageTransformer(true, SlideAnimation())
        photoSlider_mainActivity.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                if (!photoSlider_mainActivity.canScrollHorizontally(1))
                    loadPhotos()
            }

            override fun onPageSelected(position: Int) {
                Log.i("MyYandex", "ops")
                setPagerTitle(position)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        })

        refreshLayout_mainActivity.setOnRefreshListener {
            close()
            getPhotoInfo(token)
        }
        getPhotoInfo(token)
    }

    private fun setPagerTitle(position: Int) {
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

    private fun close() {
        photoManager?.close()
        photoManager = null
        infoGetter?.close()
        infoGetter = null
    }

    private fun disableRefreshing() {
        refreshLayout_mainActivity!!.isRefreshing = false
    }

    override fun onGetPhotoInfo(result: List<PhotoInfo>) {
        disableRefreshing()
        val cardAdapter = PhotoAdapter(this, 0)
        recyclerView_mainActivity.adapter = cardAdapter
        val slideAdapter = PhotoSlider(1, LayoutInflater.from(this), photoSlider_mainActivity)
        photoSlider_mainActivity.adapter = slideAdapter
        photoManager = PhotoManager(
                this,
                this,
                listOf(cardAdapter, slideAdapter),
                this,
                result
        )
        photoManager!!.start()
        loadPhotos()
    }

    private fun loadPhotos() {
        if (hasConnection())
            photoManager?.loadNextPack()
        else
            Toast
                    .makeText(this, "Проверьте подключение к интернету", Toast.LENGTH_SHORT)
                    .show()
    }

    override fun onItemClick(position: Int) {
        setSupportActionBar(sliderToolbar_mainActivity)
        setPagerTitle(position)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        photoSlider_mainActivity.setCurrentItem(position, false)
        slider_mode.visibility = View.VISIBLE
        lenta_mode.visibility = View.GONE
        sliderEnable = true
    }

    override fun onBackPressed() {
        if (!sliderEnable)
            super.onBackPressed()
        else
            enableLenta()
    }

    override fun onDestroy() {
        close()
        super.onDestroy()
    }

    override fun onNetworkConnectionError() {
        noticeAboutBadNetwork()
    }

    private fun enableLenta() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setSupportActionBar(lentaToolbar_mainActivity)
        slider_mode.visibility = View.GONE
        lenta_mode.visibility = View.VISIBLE
        sliderEnable = false

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (sliderEnable)
            enableLenta()
        return true
    }
}