package com.yandexgallery.yandexgallery

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.min


class MainActivity : AppCompatActivity(), OnGetPhotoInfoListener {
    private var photoInfo: List<Pair<String, String>>? = null
    private var loadedPhotoCount = 0
    private val photoPackCount = 10
    private val days = HashMap<String, GridView>()
    private val months = arrayListOf("Января", "Февраля", "Марта", "Апреля", "Мая",
            "Июня", "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = getSharedPreferences("AppData", Context.MODE_PRIVATE).getString("Token", "")
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar__main_activity)
        drawerLayout__mainActivity.addDrawerListener(
                ActionBarDrawerToggle(
                        this,
                        drawerLayout__mainActivity,
                        toolbar__main_activity,
                        R.string.app_name,
                        R.string.app_name)
        )
        mainScrollView.setOnScrollChangeListener { v: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            if (v!!.bottom == scrollY && photoInfo != null)
                loadPhotos()
        }
        GetPhotoInfo(token, this).execute()
    }

    override fun onGetPhotoInfo(result: List<Pair<String, String>>) {
        photoInfo = result
        loadPhotos()
    }

    private fun loadPhotos() {
        val a = min(photoInfo!!.size - 1, loadedPhotoCount + photoPackCount)
        for (i in loadedPhotoCount until a) {
            val element = photoInfo!![i]
            val day = decodeDay(element.second)
            if (!days.containsKey(day)) {
                val title = TextView(this)
                title.text = day
                title.textSize = 24 / resources.displayMetrics.scaledDensity
                title.setTextColor(ContextCompat.getColor(this, R.color.white))
                linearLayout__main_activity.addView(title)
                val gridView = ExpandableHeightGridView(this)
                gridView.adapter = PhotoAdapter(this)
                gridView.numColumns = 2
                gridView.verticalSpacing = 8
                gridView.horizontalSpacing = 8
                gridView.isExpanded = true
                gridView.isScrollContainer = false
                linearLayout__main_activity.addView(gridView)
                val lp = (gridView.layoutParams as ViewGroup.MarginLayoutParams)
                lp.setMargins(0, 10, 0, 10)
                gridView.layoutParams = lp
                days[decodeDay(element.second)] = gridView
            }
            val view = ImageView(this)
            view.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.white))
            (days[day]!!.adapter as PhotoAdapter).addElement(view, element.first)
            (days[day]!!.adapter as PhotoAdapter).notifyDataSetChanged()
        }
        loadedPhotoCount = a
    }

    private fun decodeDay(str: String): String {
        val arr = str.split("T")[0].split("-")
        return "${arr[2]} ${months[arr[1].toInt() - 1]} ${arr[0]}"
    }

}
