package com.example.holidayimage.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import com.example.holidayimage.`object`.ImageFile
import com.example.holidayimage.`object`.ImageGallery
import com.example.holidayimage.`object`.ImageItem
import com.example.holidayimage.core.db.DBFunction
import com.example.holidayimage.core.db.entity.ImageEntity
import com.example.holidayimage.utils.Constance
import com.example.holidayimage.utils.SaveImageFile


object FileDownloadManager {

    suspend fun downloadImage(context: Context, imageItem: ImageItem): ImageFile? {

        val data: Bitmap
        val response = ApiHelper.getPhoto(imageItem.raw)
        try {
            data = BitmapFactory.decodeStream(response.byteStream())
            val path = SaveImageFile.saveImageToInternalStorage(context, data, Constance.FOLDER_NAME, imageItem.url)
            return DBFunction.saveImage(imageItem.url, path)
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun isDownloaded(imageItem: ImageItem): Boolean {

        val listImageDB = DBFunction.getAllImage()
        for (image in listImageDB) {
            if (TextUtils.equals(image.url, imageItem.url)) {
                return true
            }
        }
        return false
    }

    suspend fun getAllListImage(): ArrayList<ImageGallery> {

        val listImageGallery = ArrayList<ImageGallery>()
        for (image in DBFunction.getAllImage()) {
            listImageGallery.add(ImageGallery(image.id!!, image.url, image.uri))
        }
        return listImageGallery
    }

    suspend fun deleteImage(imageGallery: ImageGallery) {
        val imageEntity = ImageEntity(imageGallery.id, imageGallery.url, imageGallery.path)
        DBFunction.deleteImageDB(imageEntity)
        SaveImageFile.deleteImageFile(imageGallery.path)
    }

    suspend fun getImageById(id: Int): ImageGallery {
        return DBFunction.getImageByID(id)
    }

}