package com.chizhikov.webimageviewier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chizhikov.webimageviewier.services.HighResImageLoaderService
import com.chizhikov.webimageviewier.services.INTENT_SERVICE_ACTION_TOKEN_BYTE_ARRAY
import com.chizhikov.webimageviewier.services.INTENT_SERVICE_ACTION_TOKEN_RESPONSE
import kotlinx.android.synthetic.main.activity_image_card.*

const val HIGH_RES_URL_TOKEN = "url"
const val POST_TEXT_TOKEN = "text"

class ImageCardActivity : AppCompatActivity() {
    private var myBroadCastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_card)

        startService(Intent(this, HighResImageLoaderService::class.java).apply {
            putExtra(HIGH_RES_URL_TOKEN, intent.getStringExtra(HIGH_RES_URL_TOKEN))
        })

        main_text.text = intent.getStringExtra(POST_TEXT_TOKEN)
        main_text.movementMethod = ScrollingMovementMethod()
        main_text.visibility = View.GONE

        post_progress_bar.visibility = View.VISIBLE

        myBroadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val byteArray = intent?.getByteArrayExtra(INTENT_SERVICE_ACTION_TOKEN_BYTE_ARRAY)
                if (byteArray != null) {
                    val byteImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    main_image.setImageBitmap(byteImage)
                    main_image.visibility = View.VISIBLE
                    main_text.visibility = View.VISIBLE
                    post_progress_bar.visibility = View.GONE
                }
            }
        }

        registerReceiver(
            myBroadCastReceiver,
            IntentFilter(INTENT_SERVICE_ACTION_TOKEN_RESPONSE).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myBroadCastReceiver)
    }
}