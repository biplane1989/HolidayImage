package com.example.holidayimage.funtion.home

import android.widget.ProgressBar
import android.widget.TextView

interface OnClicked {

    fun onClicked(position: Int, imageItemView: ImageItemView, textView: TextView, progressBar: ProgressBar)
}