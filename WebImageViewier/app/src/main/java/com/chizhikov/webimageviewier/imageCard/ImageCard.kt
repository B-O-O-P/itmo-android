package com.chizhikov.webimageviewier.imageCard

import android.graphics.Bitmap

data class ImageCard(
    var preview: Bitmap? = null,
    var description: String = "",
    var previewUrl: String = "",
    var highResUrl: String = "",
    var likeCount: Int = 0,
    var rePostCount: Int = 0
)