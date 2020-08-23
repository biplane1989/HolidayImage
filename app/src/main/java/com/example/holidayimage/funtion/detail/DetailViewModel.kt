package com.example.holidayimage.funtion.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.holidayimage.`object`.ImageGallery
import com.example.holidayimage.core.FileDownloadManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private var imageDetail: MutableLiveData<ImageGallery> = MutableLiveData()
    private lateinit var _imageDetail: ImageGallery

    fun getImage(): MutableLiveData<ImageGallery> {
        return imageDetail
    }

    fun getImageById(id: Int){
        CoroutineScope(Dispatchers.Default).launch {
            _imageDetail = FileDownloadManager.getImageById(id)
            imageDetail.postValue(_imageDetail)
        }
    }

    fun deleteImage() {
        CoroutineScope(Dispatchers.Default).launch {
            FileDownloadManager.deleteImage(_imageDetail)
        }
    }

}