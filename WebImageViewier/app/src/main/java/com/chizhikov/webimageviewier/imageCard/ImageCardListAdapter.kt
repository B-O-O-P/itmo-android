package com.chizhikov.webimageviewier.imageCard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chizhikov.webimageviewier.R

class ImageCardListAdapter(
    private val images: List<ImageCard>,
    val onClick: (ImageCard) -> Unit
) : RecyclerView.Adapter<ImageCardListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageCardListViewHolder {
        return ImageCardListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.image_list_item,
                parent,
                false
            )
        ).apply {
            root.setOnClickListener {
                onClick(images[adapterPosition]);
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImageCardListViewHolder, position: Int) {
        holder.imagePreview.setImageBitmap(images[position].preview)
        holder.imageDescription.text = images[position].description
    }
}