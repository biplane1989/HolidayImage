package com.example.holidayimage.funtion.home

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.holidayimage.R
import com.example.holidayimage.databinding.ActivityHomeScreenBinding
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.item_home.*
import kotlinx.coroutines.*

class HomeScreen : Fragment() , OnClicked {

    val TAG = "001"
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeBinding: ActivityHomeScreenBinding
    private lateinit var adapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel(activity!!.application)::class.java)
        adapter = HomeAdapter(this)
        homeViewModel.getListImage()?.observe(this , Observer { listImage ->
            listImage?.let { adapter.submitList(ArrayList(listImage)) }
            adapter.notifyDataSetChanged()
            Log.d(TAG , "onCreate: list size: " + listImage.size)
        })
    }

    override fun onCreateView(inflater: LayoutInflater , container: ViewGroup? , savedInstanceState: Bundle?): View? {

        homeBinding = DataBindingUtil.inflate(layoutInflater , R.layout.activity_home_screen , container , false)
        homeBinding.lifecycleOwner = this
        homeBinding.homeviewmodel = homeViewModel

        return homeBinding.root
    }

    private fun init() {
        rv_images.layoutManager = GridLayoutManager(context , 2 , RecyclerView.VERTICAL , false)
        rv_images.setHasFixedSize(true)
        rv_images.adapter = adapter

        initScrollListener()
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        init()
        homeViewModel.synchronizedData()
        // go to gallery screen
        fab_gallery.setOnClickListener(View.OnClickListener {
            val directions = HomeScreenDirections.actionHoneToGallery()
            NavHostFragment.findNavController(this@HomeScreen).navigate(directions)
        })
    }

    // item clicked
    override fun onClicked(position: Int , imageItemView: ImageItemView , imageView: ImageView , progressBar: ProgressBar) {
        homeViewModel.downloadImage(position)
    }

    // Scorll list and loadmore data
    private fun initScrollListener() {

        homeBinding.rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView , dx: Int , dy: Int) {
                super.onScrolled(recyclerView , dx , dy)

                val gridLayoutManager: GridLayoutManager = homeBinding.rvImages.layoutManager as GridLayoutManager
                if (homeViewModel.isLoadMore()) {
                    if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == homeViewModel.getListSize() - 1) {
                        loadMore()
                    }
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView , newState: Int) {
                super.onScrollStateChanged(recyclerView , newState)
            }
        })
    }

    private fun loadMore() {
        CoroutineScope(Dispatchers.Main).launch {
            fab_gallery.isEnabled = false
            progress_bar.visibility = View.VISIBLE
            homeViewModel.loadMore()
            progress_bar.visibility = View.INVISIBLE
            fab_gallery.isEnabled = true
        }
    }
}

