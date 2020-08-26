package com.example.holidayimage.core.db.dao

import androidx.room.*
import com.example.holidayimage.core.db.entity.ImageEntity

@Dao interface ImageDAO {
    @Query("SELECT * FROM images WHERE id = :id") suspend fun getUserById(id: Int): ImageEntity

    @Query("SELECT * FROM images WHERE url = :url") suspend fun getUserByUrl(url: String): ImageEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(image: ImageEntity): Long

    @Query("SELECT * FROM images") suspend fun getAll(): List<ImageEntity>

    @Query("DELETE from images where url = :url") suspend fun delete(url: String)

    @Delete suspend fun delete(image: ImageEntity)

    @Query("DELETE FROM images") suspend fun deleteAllImage()

    @Update suspend fun update(image: ImageEntity)
}