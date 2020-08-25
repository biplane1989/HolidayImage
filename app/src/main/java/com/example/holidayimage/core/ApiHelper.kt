package com.example.holidayimage.core

import android.graphics.BitmapFactory
import android.util.Log
import com.example.holidayimage.`object`.ImageFile
import com.example.holidayimage.`object`.ImageItem
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import kotlincodes.com.retrofitwithkotlin.retrofit.ApiClient
import okhttp3.Callback
import okhttp3.ResponseBody
import java.lang.Exception


object ApiHelper {

    suspend fun getListPhoto(page: Int): List<ImageItem>? {
        try {
            val listImage: ArrayList<UnsplashPhoto> = ApiClient.getClient.getPhotos(page)
            val listImageItem = ArrayList<ImageItem>()
            for (image in listImage) {
                val imageItem = ImageItem(image.id , image.urls.thumb!! , image.urls.raw!!)
                imageItem.downloaded = FileDownloadManager.isDownloaded(imageItem)
                listImageItem.add(imageItem)
            }
            return listImageItem
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getPhoto(url: String): ResponseBody {
        val newUrl = "$url&w=300&dpi=1"
        return ApiClient.getClient.downloadPhoto(newUrl)
    }
}