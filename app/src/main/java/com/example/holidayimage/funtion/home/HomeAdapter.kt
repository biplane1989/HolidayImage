package com.example.holidayimage.funtion.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.holidayimage.R
import java.util.concurrent.Executors

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

    class ViewHolder(itemView: View , val adapter: HomeAdapter) : RecyclerView.ViewHolder(itemView) {

        val ivServer: ImageView = itemView.findViewById(R.id.iv_server)
        val ivLoad : ImageView = itemView.findViewById(R.id.iv_download)
        val progressImage: ProgressBar = itemView.findViewById(R.id.progress_image)

        fun bind(onClicked: OnClicked) {
            val imageItem = adapter.getItem(adapterPosition)

            Glide.with(itemView.context).load(imageItem.imageItem.thumb).into(ivServer)

            if (imageItem.imageItem.downloaded) {
                ivLoad.visibility = View.INVISIBLE
            } else {
                ivLoad.visibility = View.VISIBLE
            }

            ivLoad.setOnClickListener(View.OnClickListener {
                onClicked.onClicked(adapterPosition , imageItem , ivLoad , progressImage)
            })
        }
    }
}
