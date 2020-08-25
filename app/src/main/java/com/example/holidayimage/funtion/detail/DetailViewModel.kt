package com.example.holidayimage.funtion.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.holidayimage.`object`.ImageFile
import com.example.holidayimage.core.FileDownloadManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private var imageDetail: MutableLiveData<ImageFile> = MutableLiveData()
    private lateinit var _imageDetail: ImageFile

    fun getImage(): MutableLiveData<ImageFile> {
        return imageDetail
    }

    fun getImageById(url: String) {
        CoroutineScope(Dispatchers.Default).launch {
            _imageDetail = FileDownloadManager.getImageByUrl(url)
            imageDetail.postValue(_imageDetail)
        }
    }

    fun deleteImage() {
        CoroutineScope(Dispatchers.Default).launch {
            FileDownloadManager.deleteImage(_imageDetail)
        }
    }
}