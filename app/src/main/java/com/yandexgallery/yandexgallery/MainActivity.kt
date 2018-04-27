package com.yandexgallery.yandexgallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnGetPhotoInfoListener {
    private var adapter: PhotoAdapter? = null
    private var infoGetter: Thread? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = getSharedPreferences("AppData", Context.MODE_PRIVATE)
        val token = data.getString("Token", "")

        setSupportActionBar(toolbar__mainActivity)
        val toggle = ActionBarDrawerToggle(
                this,
                drawerLayout__mainActivity,
                toolbar__mainActivity,
                R.string.app_name,
                R.string.app_name)
        drawerLayout__mainActivity.addDrawerListener(toggle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
        navigationView__mainActivity.setNavigationItemSelectedListener {
            close()
            data.edit().remove("Token").apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            true
        }

        recyclerView__mainActivity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView!!.canScrollVertically(1))
                    loadPhotos()
            }
        })
        recyclerView__mainActivity.layoutManager = LinearLayoutManager(this)

        infoGetter = Thread(PhotoInfoGetter(token, this))
        infoGetter!!.start()
    }

    private fun close() {
        adapter?.close()
        infoGetter?.interrupt()
    }

    override fun onGetPhotoInfo(result: List<PhotoInfo>) {
        adapter = PhotoAdapter(this, result, window.decorView.handler)
        recyclerView__mainActivity.adapter = adapter
        loadPhotos()
    }

    private fun loadPhotos() {
        adapter!!.loadPhotos()
    }

    override fun onDestroy() {
        close()
        super.onDestroy()
    }
}
