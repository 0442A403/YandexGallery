package com.yandexgallery.yandexgallery

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.net.NetworkInfo
import android.net.ConnectivityManager
import android.widget.Toast


@SuppressLint("Registered")
open class NetworkActivity : AppCompatActivity() {
    protected fun hasConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val mobileConnectionStatte = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .state
        val wifiConnectionState = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .state
        return mobileConnectionStatte == NetworkInfo.State.CONNECTED
                || wifiConnectionState == NetworkInfo.State.CONNECTED
    }

    protected fun noticeAboutBadNetwork() {
        Toast.makeText(this, "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show()
    }
}