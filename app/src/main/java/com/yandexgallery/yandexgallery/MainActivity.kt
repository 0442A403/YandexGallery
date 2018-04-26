package com.yandexgallery.yandexgallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.min


class MainActivity : AppCompatActivity(), OnGetPhotoInfoListener {
    private var adapter: PhotoAdapter? = null
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
            data.edit().remove("Token").apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            true
        }

        recyclerView__mainActivity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                loadPhotos()
            }
        })
        recyclerView__mainActivity.layoutManager = LinearLayoutManager(this)

        GetPhotoInfo(token, this).execute()
    }

    override fun onGetPhotoInfo(result: List<PhotoInfo>) {
        adapter = PhotoAdapter(this, result, window.decorView.handler)
        recyclerView__mainActivity.adapter = adapter
        loadPhotos()
    }

    private fun loadPhotos() {
        adapter?.loadPhotos()
        Log.i("MyYandex", "${adapter?.itemCount}")
    }
}
