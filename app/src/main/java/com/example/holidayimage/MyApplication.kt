package com.example.holidayimage

import android.app.Application
import com.example.holidayimage.core.db.ImageDatabase

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ImageDatabase.create(this)
    }

}