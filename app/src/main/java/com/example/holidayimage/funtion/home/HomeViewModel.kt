package com.example.holidayimage.funtion.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.holidayimage.`object`.ImageFile
import com.example.holidayimage.`object`.ImageItem
import com.example.holidayimage.core.ApiHelper
import com.example.holidayimage.core.FileDownloadManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    val TAG = "001"
    private var page: Int = 1;
    private var images: MutableLiveData<ArrayList<ImageItemView>> = MutableLiveData()
    private var _images = ArrayList<ImageItemView>()

    //    private val loadMoreInfo = MutableLiveData<LoadMoreInfo>()
    //    private val _LoadMoreInfo = LoadMoreInfo(LoadMoreState.DONE)

    init {
        images.value = ArrayList()
        //        initData()
        //        CoroutineScope(Dispatchers.Default).launch {
        //            refresherData()
        //        }
    }

    fun getListImage(): MutableLiveData<ArrayList<ImageItemView>> {
        return images
    }

    fun getListSize(): Int {
        return images.value!!.size
    }

    suspend fun refresherData() {
        page = 1
        _images.clear()
        getData()
    }

    fun initData() {
        CoroutineScope(Dispatchers.Default).launch {
            for (item in ApiHelper.getListPhoto(page)) {
                _images.add(ImageItemView(item))
            }
            images.postValue(_images)
        }
    }

    suspend fun getData() {
        for (item in ApiHelper.getListPhoto(page)) {
            _images.add(ImageItemView(item))
        }
        page++;
        images.postValue(_images)
    }

    suspend fun saveImage(context: Context , imageItem: ImageItem , position: Int): ImageFile? {
        return FileDownloadManager.downloadImage(context , imageItem)
        //        val imageFile = FileDownloadManager.downloadImage(context, imageItem)
        //        if (imageFile == null) {
        //
        //        }
    }

    suspend fun synchronizedData() {
        CoroutineScope(Dispatchers.Main).launch {
            val newListImage = ArrayList<ImageItemView>()
            for (image in _images) {
                newListImage.add(image)
            }
            var position = 0;
            for (image in newListImage) {
                if (FileDownloadManager.isDownloaded(ImageItem(image.imageItem.url , image.imageItem.thumb , image.imageItem.raw , image.imageItem.downloaded))) {
                    val newImage = image.copy()
                    newImage.imageItem.downloaded = true
                    newListImage.set(position , newImage)
                }
                position++
            }
            _images = newListImage
            images.postValue(_images)
        }
    }
}