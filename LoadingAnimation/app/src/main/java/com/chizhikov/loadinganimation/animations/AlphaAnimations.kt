package com.chizhikov.loadinganimation.animations

import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator

fun inifiniteAlphaAnimation(alphaIn: Float, alphaEnd: Float, alphaDuration: Long): AlphaAnimation {
    return AlphaAnimation(alphaIn, alphaEnd).apply {
        duration = alphaDuration
        repeatCount = AlphaAnimation.INFINITE
        repeatMode = AlphaAnimation.REVERSE
        interpolator = LinearInterpolator()
    }
}