package com.example.holidayimage.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import com.example.holidayimage.`object`.ImageFile
import com.example.holidayimage.`object`.ImageItem
import com.example.holidayimage.core.db.DBFunction
import com.example.holidayimage.core.db.entity.ImageEntity
import com.example.holidayimage.utils.Constance
import com.example.holidayimage.utils.SaveImageFile


object FileDownloadManager {

    suspend fun downloadImage(context: Context , imageItem: ImageItem): ImageFile? {

        val data: Bitmap
        try {
            val response = ApiHelper.getPhoto(imageItem.raw)
            data = BitmapFactory.decodeStream(response.byteStream())
            val path = SaveImageFile.saveImageToInternalStorage(context , data , Constance.FOLDER_NAME , imageItem.url)
            return DBFunction.saveImage(imageItem.url , path)
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun isDownloaded(imageItem: ImageItem): Boolean {

        val listImageDB = DBFunction.getAllImage()
        for (image in listImageDB) {
            if (TextUtils.equals(image.url , imageItem.url)) {
                return true
            }
        }
        return false
    }

    suspend fun getAllListImage(): ArrayList<ImageFile> {

        val listImageGallery = ArrayList<ImageFile>()
        for (image in DBFunction.getAllImage()) {
            listImageGallery.add(ImageFile(image.url , image.path))
        }
        return listImageGallery
    }

    suspend fun deleteImage(imageFile: ImageFile) {
        DBFunction.deleteImageDB(imageFile)
        SaveImageFile.deleteImageFile(imageFile.path)
    }

    suspend fun getImageByUrl(url: String): ImageFile {
        return DBFunction.getImageByUrl(url)
    }

}