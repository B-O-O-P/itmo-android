package com.chizhikov.loadinganimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chizhikov.loadinganimation.animations.inifiniteAlphaAnimation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loading_text.startAnimation(inifiniteAlphaAnimation(0F, 1F, 1000L))
    }

    override fun onStop() {
        super.onStop()
        loading_text.animation.cancel()
    }
}
