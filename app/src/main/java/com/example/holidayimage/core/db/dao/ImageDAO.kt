package com.example.holidayimage.core.db.dao

import androidx.room.*
import com.example.holidayimage.core.db.entity.ImageEntity

@Dao interface ImageDAO {
    @Query("SELECT * FROM images WHERE id = :id") suspend fun getUserById(id: Int): ImageEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(image: ImageEntity): Long

    @Query("SELECT * FROM images") suspend fun getAll(): List<ImageEntity>

    @Delete suspend fun delete(image: ImageEntity)

    @Query("DELETE FROM images") suspend fun deleteAllImage()

    @Update suspend fun update(image: ImageEntity)
}