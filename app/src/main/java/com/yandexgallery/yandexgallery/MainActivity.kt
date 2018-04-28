package com.yandexgallery.yandexgallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnGetPhotoInfoListener {
    private var adapter: PhotoAdapter? = null
    private var infoGetter: PhotoInfoGetter? = null
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
        infoGetter = PhotoInfoGetter(token, this)
        infoGetter!!.start()
    }

    private fun close() {
        adapter?.close()
        infoGetter?.close()
        infoGetter = null
    }

    override fun onGetPhotoInfo(result: List<PhotoInfo>) {
        refreshLayout_mainActivity!!.isRefreshing = false
        adapter = PhotoAdapter(this, result)
        recyclerView_mainActivity.adapter = adapter
        recyclerView_mainActivity.layoutManager = LinearLayoutManager(this)
        loadPhotos()
    }

    private fun loadPhotos() {
        adapter?.loadPhotos()
    }

    override fun onDestroy() {
        close()
        super.onDestroy()
    }
}
