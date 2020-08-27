package com.example.holidayimage

import android.app.Activity
import android.app.Application
import androidx.multidex.MultiDexApplication
import com.example.holidayimage.core.db.ImageDatabase

class MyApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        ImageDatabase.create(this)
    }
}