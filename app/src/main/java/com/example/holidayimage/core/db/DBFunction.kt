package com.example.holidayimage.core.db

import com.example.holidayimage.`object`.ImageFile
import com.example.holidayimage.core.db.entity.ImageEntity

object DBFunction {

    suspend fun saveImage(url: String , filePath: String): ImageFile? {
        val imageEntity = ImageEntity(null , url , filePath)
        val id = ImageDatabase.getInstance().imageDAO().insert(imageEntity)
        if (id > 0) {
            return ImageFile(url , filePath)
        } else {
            return null;
        }
    }

    suspend fun getAllImage(): List<ImageEntity> {
        return ImageDatabase.getInstance().imageDAO().getAll()
    }

    suspend fun deleteImageDB(imageFile: ImageFile) {
        ImageDatabase.getInstance().imageDAO().delete(imageFile.url)
    }

    suspend fun getImageByUrl(url : String): ImageFile{
        val imageEntity = ImageDatabase.getInstance().imageDAO().getUserByUrl(url)
        return ImageFile(imageEntity.url , imageEntity.path)
    }
}