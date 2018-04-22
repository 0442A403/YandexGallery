package com.yandexgallery.yandexgallery

import android.os.AsyncTask
import android.util.Log
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.json.JSONObject
import java.util.*

class GetPhotoInfo(private val token: String, private val callback: OnGetPhotoInfoListener) :
        AsyncTask<Void, Void, List<Pair<String, String>>>() {
    private val default = "https://cloud-api.yandex.net/v1/disk/resources"
    override fun doInBackground(vararg params: Void?): List<Pair<String, String>> {
        val array = getFiles("/")
        Log.i("MyYandex", array.toString())
        return array
    }

    private fun getFiles(path: String): ArrayList<Pair<String, String>> {
        val response = OkHttpClient()
                .newCall(
                        Request.Builder()
                                .addHeader("Authorization", token)
                                .url("$default?path=$path&fields=_embedded.items.path," +
                                        "_embedded.items.media_type,_embedded.items.type," +
                                        "_embedded.items.created,_embedded.items.file," +
                                        "_embedded.items.name&sort=created")
                                .build())
                .execute()
        val array = JSONObject(response.body().string())
                .getJSONObject("_embedded")
                .getJSONArray("items")
        val result = ArrayList<Pair<String, String>>()
        for (i in 0 until array.length()) {
            val element = array[i] as JSONObject
            when {
                element["type"] == "file" && (element["media_type"] as String) == "image" ->
                        result.add(Pair(element["file"] as String, element["created"] as String))
                element["type"] == "dir" ->
                        result.addAll(getFiles(element["path"] as String))
             }
        }
        return result
    }

    override fun onPostExecute(result: List<Pair<String, String>>?) {
        super.onPostExecute(result)
        callback.onGetPhotoInfo(result!!)
    }
}