package com.example.holidayimage.funtion.home

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

interface OnClicked {

    fun onClicked(position: Int, imageItemView: ImageItemView, imageView: ImageView, progressBar: ProgressBar)
}