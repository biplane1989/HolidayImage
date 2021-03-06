package com.example.holidayimage.funtion.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.holidayimage.R
import com.example.holidayimage.`object`.ImageFile
import com.example.holidayimage.`object`.ImageItem
import com.example.holidayimage.core.ApiHelper
import com.example.holidayimage.core.FileDownloadManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private var page: Int = 1
    private var images: MutableLiveData<ArrayList<ImageItemView>> = MutableLiveData()
    private var _images = ArrayList<ImageItemView>()
    private var statusLoadMore: Boolean = true

    fun isLoadMore(): Boolean {
        return statusLoadMore
    }

    fun resetIsLoadMore() {
        statusLoadMore = true
        images.postValue(_images)
    }

    init {
        images.value = _images
        statusLoadMore = true
    }

    fun getListImage(): MutableLiveData<ArrayList<ImageItemView>>? {
        return images
    }

    fun getListSize(): Int {
        return images.value!!.size
    }

    suspend fun getData() {
        if (ApiHelper.getListPhoto(page).size > 1) {
            for (item in ApiHelper.getListPhoto(page)) {
                val imageItemView = ImageItemView(item)
                _images.add(imageItemView)
            }
            page++
            statusLoadMore = true

            images.postValue(_images)
        } else {
            Toast.makeText(context , R.string.server_error , Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun loadMore() {
        if (isNetworkConnected()) {
            statusLoadMore = false
            getData()

        } else {
            Toast.makeText(context , R.string.title_notification , Toast.LENGTH_SHORT).show()
        }
    }

    fun downloadImage(position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            if (_images.get(position).clicked) {
                return@launch
            }
            _images.get(position).clicked = true
            var imageItemView = _images.get(position).copy()
            imageItemView.isDownloading = true
            _images.set(position , imageItemView)

            images.postValue(_images)
            if (saveImage(context , imageItemView.imageItem) == null) {
                Toast.makeText(context , R.string.title_download_unsuccessful , Toast.LENGTH_SHORT).show()
                _images.get(position).clicked = false
            } else {
                Toast.makeText(context , R.string.title_download_successful , Toast.LENGTH_SHORT).show()
            }

            imageItemView = _images.get(position).copy()
            imageItemView.isDownloading = false
            imageItemView.imageItem.downloaded = FileDownloadManager.isDownloaded(imageItemView.imageItem)

            _images.set(position , imageItemView)
            images.postValue(_images)
        }
    }

    fun synchronizedData() {
        CoroutineScope(Dispatchers.Main).launch {
            var position = 0
            for (image in _images) {
                if (!FileDownloadManager.isDownloaded(image.imageItem)) {

                    val newImage = image.copy()
                    newImage.isDownloading = false
                    val newImageItem = newImage.imageItem.copy()
                    newImageItem.downloaded = false
                    newImage.imageItem = newImageItem
                    _images.set(position , newImage)
                }
                position++
            }
            images.postValue(_images)
        }
    }

    suspend fun saveImage(context: Context , imageItem: ImageItem): ImageFile? {
        return FileDownloadManager.downloadImage(context , imageItem)
    }

    fun isNetworkConnected(): Boolean {
        val cm: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo()!!.isConnected()
    }

    suspend fun checkStatusServer(): Boolean {
        if (ApiHelper.getListPhoto(1) == null) return false
        return true
    }
}