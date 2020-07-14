package com.chizhikov.webimageviewier

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chizhikov.webimageviewier.imageCard.ImageCard
import com.chizhikov.webimageviewier.imageCard.ImageCardListAdapter
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL

const val ACCESS_SERVICE_TOKEN: String =
    "63824fa963824fa963824fa9e263ef1dfc6638263824fa93e1fbffe839b46cf5d6bbb65"

class MainActivity : AppCompatActivity() {

    private var asyncTask: ImageLoader? = null
    private var queryList: List<ImageCard>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edit_query.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                executeQuery(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        query_progress_bar.visibility = View.GONE

        setImageCardList(queryList)

        asyncTask = lastCustomNonConfigurationInstance as? ImageLoader
        asyncTask?.attachActivity(this)
    }

    override fun onRetainCustomNonConfigurationInstance(): ImageLoader? {
        return asyncTask
    }

    override fun onDestroy() {
        asyncTask?.activity = null
        super.onDestroy()
    }

    private fun getQueryUrl(query: String): String {
        val attributes = mapOf(
            "q" to query,
            "count" to "50",
            "sort" to "0",
            "v" to "5.102"
        )
        return "https://api.vk.com/method/photos.search?access_token=$ACCESS_SERVICE_TOKEN&${
        attributes.map { it.key + "=" + it.value }.reduce { att, acc -> "$acc&$att" }}"
    }

    private fun executeQuery(query: String) {
        asyncTask?.cancel(true)
        asyncTask?.activity = null
        asyncTask = ImageLoader(this).apply {
            execute(getQueryUrl(query))
        }
    }

    private fun setImageCardList(imageCardList: List<ImageCard>?) {
        queryList = imageCardList
        if (imageCardList != null) {
            image_recycler_view.visibility = View.VISIBLE
            image_recycler_view.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = ImageCardListAdapter(imageCardList) {
                    startActivity(Intent(this@MainActivity, ImageCardActivity::class.java).apply {
                        putExtra(HIGH_RES_URL_TOKEN, it.highResUrl)
                        putExtra(POST_TEXT_TOKEN, it.description)
                    })
                }
            }
        }
    }

    internal fun onLoadCompleted(result: List<ImageCard>?) {
        if (result != null) {
            setImageCardList(result)
        }
        asyncTask = null
    }

    class ImageLoader(var activity: MainActivity?) :
        AsyncTask<String, Int, List<ImageCard>>() {
        private var cachedResult: List<ImageCard>? = null

        override fun onPreExecute() {
            super.onPreExecute()
            activity?.apply {
                image_recycler_view.visibility = View.GONE
                query_progress_bar.visibility = View.VISIBLE
                query_progress_bar.progress = 0
                query_progress_bar.max = 50
            }
        }

        override fun doInBackground(vararg params: String?): List<ImageCard> {
            return getListFromResponse(URL(params[0]).openConnection().getInputStream().reader().readText())
        }

        override fun onPostExecute(result: List<ImageCard>?) {
            activity?.query_progress_bar!!.visibility = View.GONE
            activity?.image_recycler_view!!.visibility = View.VISIBLE
            activity?.onLoadCompleted(result) ?: run {
                cachedResult = result
            }
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            activity?.query_progress_bar?.apply {
                visibility = View.VISIBLE
                progress = values[0]!!
            }
        }


        fun attachActivity(activity: MainActivity) {
            this.activity = activity

            cachedResult?.let {
                activity.onLoadCompleted(it)
                cachedResult = null
            }
        }

        private fun getListFromResponse(response: String?): List<ImageCard> {
            if (response == null) {
                return emptyList()
            } else {
                val json = Gson().fromJson(response, JsonObject::class.java)
                if (!json.has("response")) {
                    return emptyList()
                }

                val jsonResponse = json.get("response").asJsonObject
                if (!jsonResponse.has("items")) {
                    return emptyList()
                }

                val items = jsonResponse.get("items").asJsonArray
                val count = items.size()
                val responseResult: ArrayList<ImageCard> = ArrayList()
                for (i in 0 until count) {
                    val item = items[i].asJsonObject
                    responseResult.add(ImageCard().apply {
                        val imageSizes = item.get("sizes").asJsonArray
                        if (item.has("text")) {
                            description = item.get("text").asString
                        }
                        previewUrl = imageSizes[0].asJsonObject.get("url").asString
                        highResUrl =
                            imageSizes[imageSizes.size() - 1].asJsonObject.get("url").asString
                        preview = downloadPreview(previewUrl)
                    })
                    publishProgress(i)
                }
                return responseResult
            }
        }

        private fun downloadPreview(previewUrl: String): Bitmap {
            return BitmapFactory.decodeStream(URL(previewUrl).openConnection().getInputStream())
        }
    }

}

