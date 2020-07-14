package com.chizhikov.webimageviewier.imageCard

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.image_list_item.view.*

class ImageCardListViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
    val imagePreview: ImageView = root.image_preview
    val imageDescription: TextView = root.image_description
}