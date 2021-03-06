package com.example.holidayimage.funtion.home

import androidx.recyclerview.widget.DiffUtil

class ImageDiffCallBack : DiffUtil.ItemCallback<ImageItemView>() {

    override fun areItemsTheSame(oldItemView: ImageItemView , newItemView: ImageItemView): Boolean {
        return oldItemView == newItemView
    }

    override fun areContentsTheSame(oldItemView: ImageItemView , newItemView: ImageItemView): Boolean {
        return oldItemView == newItemView
    }
}
