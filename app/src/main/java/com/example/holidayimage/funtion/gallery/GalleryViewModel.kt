package com.example.holidayimage.funtion.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.holidayimage.`object`.ImageFile
import com.example.holidayimage.core.FileDownloadManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {

    private var images: MutableLiveData<ArrayList<ImageFile>> = MutableLiveData()
    private var _images = ArrayList<ImageFile>()

    init {
        images.value = _images
    }

    fun getListImage(): MutableLiveData<ArrayList<ImageFile>> {
        return images
    }

    fun initData() {
        CoroutineScope(Dispatchers.Default).launch {
            for (item in FileDownloadManager.getAllListImage()) {
                _images.add(item)
            }
            images.postValue(_images)
        }
    }

    fun synchronizedData() {
        CoroutineScope(Dispatchers.Default).launch {
            val newListImage = ArrayList<ImageFile>()
            for (item in FileDownloadManager.getAllListImage()) {
                newListImage.add(item)
            }
            images.postValue(newListImage)
        }
    }
}