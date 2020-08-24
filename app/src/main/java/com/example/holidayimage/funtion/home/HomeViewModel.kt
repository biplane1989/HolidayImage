package com.example.holidayimage.funtion.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.holidayimage.R
import com.example.holidayimage.`object`.ImageFile
import com.example.holidayimage.`object`.ImageItem
import com.example.holidayimage.core.ApiHelper
import com.example.holidayimage.core.FileDownloadManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private var page: Int = 1;
    private var images: MutableLiveData<ArrayList<ImageItemView>> = MutableLiveData()
    private var _images = ArrayList<ImageItemView>()
    private var statusLoadMore: Boolean = true

    fun isLoadmore(): Boolean {
        return statusLoadMore
    }

    private lateinit var errorDownload: ErrorDownload

    fun setCallBack(errorDownload: ErrorDownload) {
        this.errorDownload = errorDownload
    }

    init {
        images.value = ArrayList()
        //        getData()
        statusLoadMore = true
    }

    fun getListImage(): MutableLiveData<ArrayList<ImageItemView>>? {
        if (isNetworkConnected()) {
            CoroutineScope(Dispatchers.Default).launch {
                getData()
            }
            return images
        } else {
            Toast.makeText(context , R.string.title_notification , Toast.LENGTH_SHORT).show()
            return null
        }

    }

    fun getListSize(): Int {
        return images.value!!.size
    }

    suspend fun refresherData() {
        page = 1
        _images.clear()
        getData()
    }

    suspend fun getData() {
        for (item in ApiHelper.getListPhoto(page)) {
            _images.add(ImageItemView(item))
        }
        page++;

        //            statusLoadMore.postValue(true)
        statusLoadMore = true

        images.postValue(_images)
    }

    suspend fun loadMore() {
        if (isNetworkConnected()) {
            //            statusLoadMore.postValue(false)
            statusLoadMore = false
            getData()

        } else {
            Toast.makeText(context , R.string.title_notification , Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun isSaveImage(position: Int , imageItemView: ImageItemView) {

        var imageItemView = _images.get(position).copy()
        imageItemView.isDownloading = true

        _images.set(position , imageItemView)
        images.postValue(_images)

        if (isNetworkConnected()) {
            if (saveImage(context , imageItemView.imageItem , position) == null) {
                //            imageView.isEnabled = true
                //                errorDownload.errorDownloadImage()
                Toast.makeText(context , R.string.title_download_unsuccessful , Toast.LENGTH_SHORT).show()
            } else {
             /*   synchronizedData()*/
                //            imageView.visibility = View.INVISIBLE
                //                errorDownload.downloadImage()
                Toast.makeText(context , R.string.title_download_successful , Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context , R.string.title_notification , Toast.LENGTH_SHORT).show()
        }
        imageItemView = _images.get(position).copy()
        imageItemView.isDownloading = false
        imageItemView.imageItem.downloaded = FileDownloadManager.isDownloaded(imageItemView.imageItem)

        _images.set(position , imageItemView)
        images.postValue(_images)
    }

    suspend fun saveImage(context: Context , imageItem: ImageItem , position: Int): ImageFile? {
        return FileDownloadManager.downloadImage(context , imageItem)
    }

   /* suspend fun synchronizedData() {
        CoroutineScope(Dispatchers.Main).launch {
            val newListImage = ArrayList<ImageItemView>()
            for (image in _images) {
                newListImage.add(image)
            }
            var position = 0
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
*/
    private fun isNetworkConnected(): Boolean {
        val cm: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo()!!.isConnected()
    }
}