package com.example.holidayimage.funtion.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.holidayimage.R
import com.example.holidayimage.`object`.ImageFile
import java.util.concurrent.*


class GalleryAdapter(val onClicked: OnGalleryClicked) :
    ListAdapter<ImageFile, GalleryAdapter.ViewHolder>(AsyncDifferConfig.Builder<ImageFile>(GalleryDiffCallBack())
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor()).build()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(onClicked)
    }

    class ViewHolder(itemView: View, val adapter: GalleryAdapter) :
        RecyclerView.ViewHolder(itemView) {

        val ivGallery: ImageView = itemView.findViewById(R.id.iv_gallery)

        fun bind(onClicked: OnGalleryClicked) {
            val imageItem = adapter.getItem(adapterPosition)

            Glide.with(itemView.context).load(imageItem.path).into(ivGallery)

            itemView.setOnClickListener(View.OnClickListener {
                onClicked.onClick(imageItem, adapterPosition)
            })
        }
    }
}