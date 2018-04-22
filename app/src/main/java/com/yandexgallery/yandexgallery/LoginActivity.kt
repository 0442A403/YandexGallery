package com.yandexgallery.yandexgallery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private var webView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (getSharedPreferences("AppData", Context.MODE_PRIVATE).contains("Token"))
            start()
        yandexLogo__launcher.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN)
                yandexLogoContainer__launcher.setPadding(10, 10, 10, 10)
            super.onTouchEvent(event)
        }
    }

    private fun start() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun login(v: View) {
        webView = WebView(this)
        webView!!.clearCache(true)
        setContentView(webView)
        webView!!.settings.javaScriptEnabled = true
        webView!!.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
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

    override fun onBackPressed() {
        when {
            webView == null -> super.onBackPressed()
            webView!!.canGoBack() -> webView!!.goBack()
            else -> setContentView(R.layout.activity_login)
        }
    }
}
