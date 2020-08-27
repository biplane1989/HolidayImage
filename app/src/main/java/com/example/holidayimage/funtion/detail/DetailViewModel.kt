package com.example.holidayimage.funtion.detail

import android.app.Application
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment
import com.example.holidayimage.R
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