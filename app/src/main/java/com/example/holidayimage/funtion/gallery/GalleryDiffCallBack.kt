package com.example.holidayimage.funtion.gallery

import androidx.recyclerview.widget.DiffUtil
import com.example.holidayimage.`object`.ImageGallery


class GalleryDiffCallBack : DiffUtil.ItemCallback<ImageGallery>() {

    override fun areItemsTheSame(oldItem: ImageGallery, newItem: ImageGallery): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImageGallery, newItem: ImageGallery): Boolean {
        return oldItem.equals(newItem)
    }
}