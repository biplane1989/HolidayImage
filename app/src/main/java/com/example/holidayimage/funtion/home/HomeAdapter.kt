package com.example.holidayimage.funtion.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.holidayimage.R
import java.util.concurrent.*

class HomeAdapter(val onClicked: OnClicked) : ListAdapter<ImageItemView , HomeAdapter.ViewHolder>(
    AsyncDifferConfig.Builder(ImageDiffCallBack()).setBackgroundThreadExecutor(Executors.newSingleThreadExecutor()).build()
) {

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): HomeAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_home , parent , false)
        return ViewHolder(itemView , this)
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder , position: Int) {
        holder.bind(onClicked)
    }

    override fun submitList(list: MutableList<ImageItemView>?) {

        if (list != null) {
            super.submitList(ArrayList(list))
        } else {
            super.submitList(ArrayList())
        }
    }

    class ViewHolder(itemView: View , val adapter: HomeAdapter) : RecyclerView.ViewHolder(itemView) {

        val ivServer: ImageView = itemView.findViewById(R.id.iv_server)
        val ivLoad: ImageView = itemView.findViewById(R.id.iv_download)
        val progressImage: ProgressBar = itemView.findViewById(R.id.progress_image)
        private var mLastClickTime = System.currentTimeMillis()
        private val CLICK_TIME_INTERVAL: Long = 800

        fun bind(onClicked: OnClicked) {
            val imageItem = adapter.getItem(adapterPosition)
            progressImage.visibility = View.INVISIBLE
            ivLoad.visibility = View.VISIBLE

            Glide.with(itemView.context).load(imageItem.imageItem.thumb).transition(DrawableTransitionOptions.withCrossFade()).dontAnimate().into(ivServer)

            if (imageItem.imageItem.downloaded) {
                ivLoad.visibility = View.GONE
                progressImage.visibility = View.INVISIBLE
            } else {
                if (imageItem.isDownloading) {
                    ivLoad.visibility = View.GONE
                    progressImage.visibility = View.VISIBLE
                } else {
                    ivLoad.visibility = View.VISIBLE
                    progressImage.visibility = View.INVISIBLE
                }
            }

            ivLoad.setOnClickListener(View.OnClickListener {
                val now = System.currentTimeMillis()
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return@OnClickListener
                }
                mLastClickTime = now
                onClicked.onClicked(adapterPosition , imageItem , ivLoad , progressImage)
            })
        }
    }
}
