package com.example.holidayimage.`object`

import com.example.holidayimage.core.db.entity.ImageEntity

enum class DownloadStatus{
    LOADING,
    DONE,
    FAIL
}
data class DownloadInfo(var imageFile: ImageEntity, var status: DownloadStatus)