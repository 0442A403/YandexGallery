package com.yandexgallery.yandexgallery

import android.os.AsyncTask
import android.util.Log
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.json.JSONObject
import java.util.*

class GetPhotoInfo(private val token: String,
                   private val callback: OnGetPhotoInfoListener) :
        AsyncTask<Void, Void, List<PhotoInfo>>() {
    private val default = "https://cloud-api.yandex.net/v1/disk/resources"
    private val months = arrayListOf("Января", "Февраля", "Марта", "Апреля", "Мая",
            "Июня", "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря")
    override fun doInBackground(vararg params: Void?): List<PhotoInfo> {
        val array = getFiles("/")
                .sortedBy { it.created }
                .reversed()
                .map { PhotoInfo(decodeDay(it.created), it.link, it.name) }
        Log.i("MyYandex", array.toString())
        return array
    }

    private fun getFiles(path: String): ArrayList<PhotoInfo> {
        val response = OkHttpClient()
                .newCall(
                        Request.Builder()
                                .addHeader("Authorization", token)
                                .url("$default?path=$path&fields=_embedded.items.path," +
                                        "_embedded.items.media_type,_embedded.items.type," +
                                        "_embedded.items.created,_embedded.items.file," +
                                        "_embedded.items.name")
                                .build())
                .execute()
        val array = JSONObject(response.body().string())
                .getJSONObject("_embedded")
                .getJSONArray("items")
        Log.i("MyYandex", array.toString())
        val result = ArrayList<PhotoInfo>()
        for (i in 0 until array.length()) {
            val element = array[i] as JSONObject
            when {
                element["type"] == "file" && (element["media_type"] as String) == "image" ->
                        result.add(PhotoInfo(element["created"] as String,
                                element["file"] as String,
                                element["name"] as String))
                element["type"] == "dir" ->
                        result.addAll(getFiles(element["path"] as String))
             }
        }
        return result
    }

    override fun onPostExecute(result: List<PhotoInfo>?) {
        super.onPostExecute(result)
        callback.onGetPhotoInfo(result!!)
    }

    private fun decodeDay(str: String): String {
        val arr = str.split("T")[0].split("-")
        return "${arr[2]} ${months[arr[1].toInt() - 1]} ${arr[0]}"
    }
}