package com.example.holidayimage.funtion.home

import android.util.Log
import androidx.recyclerview.widget.DiffUtil

class ImageDiffCallBack : DiffUtil.ItemCallback<ImageItemView>() {

    //vi tri
    override fun areItemsTheSame(oldItemView: ImageItemView , newItemView: ImageItemView): Boolean {
        return oldItemView == newItemView
    }

    // noi dung
    override fun areContentsTheSame(oldItemView: ImageItemView , newItemView: ImageItemView): Boolean {
        return oldItemView == newItemView
    }
}
