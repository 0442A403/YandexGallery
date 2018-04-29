package com.yandexgallery.yandexgallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : NetworkActivity(), OnGetPhotoInfoListener, OnNetworkConnectionErrorListener {
    private var infoGetter: PhotoInfoGetter? = null
    private var photoManager: PhotoManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = getSharedPreferences("AppData", Context.MODE_PRIVATE)
        val token = data.getString("Token", "")

        setSupportActionBar(toolbar_mainActivity)
        val toggle = ActionBarDrawerToggle(
                this,
                drawerLayout_mainActivity,
                toolbar_mainActivity,
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

        recyclerView_mainActivity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView!!.canScrollVertically(1))
                    loadPhotos()
            }
        })

        refreshLayout_mainActivity.setOnRefreshListener {
            close()
            getPhotoInfo(token)
        }
        getPhotoInfo(token)
    }

    private fun getPhotoInfo(token: String) {
        if (hasConnection()) {
            infoGetter = PhotoInfoGetter(token, this, this)
            infoGetter!!.start()
        }
        else {
            disableRefreshing()
            noticeAboutNetwork()
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
        val adapter = PhotoAdapter(this, 0)
        recyclerView_mainActivity.adapter = adapter
        recyclerView_mainActivity.layoutManager = LinearLayoutManager(this)
        photoManager = PhotoManager(this, this, listOf(adapter), result)
        photoManager!!.start()
        loadPhotos()
    }

    private fun loadPhotos() {
        if (hasConnection())
            photoManager?.loadNextPack()
        else
            Toast.makeText(this, "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        close()
        super.onDestroy()
    }

    override fun onNetworkConnectionError() {
        noticeAboutNetwork()
    }
}
