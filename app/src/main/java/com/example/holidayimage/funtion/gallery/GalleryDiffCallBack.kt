package com.example.holidayimage.funtion.gallery

import androidx.recyclerview.widget.DiffUtil
import com.example.holidayimage.`object`.ImageFile


class GalleryDiffCallBack : DiffUtil.ItemCallback<ImageFile>() {

    override fun areItemsTheSame(oldItem: ImageFile , newItem: ImageFile): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ImageFile , newItem: ImageFile): Boolean {
        return oldItem.equals(newItem)
    }
}