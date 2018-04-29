package com.yandexgallery.yandexgallery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_login.*
import android.webkit.CookieSyncManager
import android.os.Build


class LoginActivity : NetworkActivity(), OnNetworkConnectionErrorListener {
    private var webView: WebView? = null
    private val touchPadding = 10
    private val defaultPadding = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (getSharedPreferences("AppData", Context.MODE_PRIVATE).contains("Token"))
            start()
        yandexLogo__launcher.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN)
                yandexLogoContainer__launcher
                        .setPadding(touchPadding,
                                touchPadding,
                                touchPadding,
                                touchPadding)
            super.onTouchEvent(event)
        }
    }

    private fun start() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun login(v: View) {
        if (hasConnection()) {
            clearCookies()
            webView = webView_launcher
            webView!!.clearCache(true)
            contentContainer_launcher.visibility = View.GONE
            webView_launcher.visibility = View.VISIBLE
            webView!!.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    if (url != null && url.contains("access_token")) {
                        getSharedPreferences("AppData", Context.MODE_PRIVATE)
                                .edit()
                                .putString(
                                        "Token",
                                        url.split("access_token=")[1]
                                                .split("&")[0])
                                .apply()
                        start()
                    }
                }
            }
            webView!!.loadUrl(getString(R.string.OAuthURL))
        }
        else {
            noticeAboutNetwork()
        }
    }

    override fun onBackPressed() {
        when {
            webView == null -> super.onBackPressed()
            webView!!.canGoBack() -> webView!!.goBack()
            else -> {
                yandexLogoContainer__launcher.setPadding(defaultPadding,
                        defaultPadding,
                        defaultPadding,
                        defaultPadding)
                contentContainer_launcher.visibility = View.VISIBLE
                webView_launcher.visibility = View.GONE
            }
        }
    }

    override fun onNetworkConnectionError() {
        noticeAboutNetwork()
    }

    private fun clearCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        } else {
            val cookieSyncManager = CookieSyncManager.createInstance(this)
            cookieSyncManager.startSync()
            val cookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()
            cookieManager.removeSessionCookie()
            cookieSyncManager.stopSync()
            cookieSyncManager.sync()
        }
    }
}
