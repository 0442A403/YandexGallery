package com.yandexgallery.yandexgallery

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.json.JSONObject

class PhotoInfoGetter(private val token: String,
                      private val callback: OnGetPhotoInfoListener,
                      private val errorCallback: OnNetworkConnectionErrorListener) : Thread() {

    private val link = "https://cloud-api.yandex.net:443/v1/disk/resources/last-uploaded?" +
            "fields=file,name,created,media_type,type"
    private val months = arrayListOf("Января", "Февраля", "Марта", "Апреля", "Мая",
            "Июня", "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря")
    private var isRunning = true

    override fun run() {
        try {
            val response = OkHttpClient()
                    .newCall(Request.Builder()
                            .addHeader("Authorization", token)
                            .url(link)
                            .build()).execute()
            if (!isRunning)
                return
            val array = JSONObject(response.body().string()).getJSONArray("items")
            Log.i("YandexResponse", "YaResponse: $array")
            val res = ArrayList<PhotoInfo>()
            for (i in 0 until array.length()) {
                val file = array.getJSONObject(i)
                if (file.getString("type") == "file"
                        && file.getString("media_type") == "image")
                    res.add(PhotoInfo(decodeDay(file["created"] as String),
                            file["file"] as String,
                            file["name"] as String))
            }
            runOnUI {
                if (isRunning)
                    callback.onGetPhotoInfo(res)
            }
        } catch (e : Exception) {
            runOnUI {
                errorCallback.onNetworkConnectionError()
            }
        }

    }

    private fun runOnUI(body: () -> Unit) {
        Handler(Looper.getMainLooper()).post(body)
    }

    fun close() {
        isRunning = false
    }

    private fun decodeDay(str: String): String {
        val arr = str.split("T")[0].split("-")
        return "${arr[2]} ${months[arr[1].toInt() - 1]} ${arr[0]}"
    }
}