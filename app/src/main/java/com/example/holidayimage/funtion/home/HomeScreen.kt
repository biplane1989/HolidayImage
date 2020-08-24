package com.example.holidayimage.funtion.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.holidayimage.MyApplication
import com.example.holidayimage.R
import com.example.holidayimage.databinding.ActivityHomeScreenBinding
import com.example.holidayimage.utils.Constance
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.coroutines.*

class HomeScreen : Fragment() , OnClicked {

    val TAG = "001"
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeBinding: ActivityHomeScreenBinding
    private lateinit var adapter: HomeAdapter
    private var statusLoadMore = true

    override fun onCreateView(inflater: LayoutInflater , container: ViewGroup? , savedInstanceState: Bundle?): View? {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeBinding = DataBindingUtil.inflate(layoutInflater , R.layout.activity_home_screen , container , false)
        homeBinding.lifecycleOwner = this
        homeBinding.homeviewmodel = homeViewModel

        return homeBinding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        init()
        if (isNetworkConnected()) {
            CoroutineScope(Dispatchers.Main).launch {
                homeViewModel.getListImage().observe(viewLifecycleOwner , Observer { listImage ->
                    adapter.submitList(ArrayList(listImage))
                    Log.d(TAG , "getListSize: list size " + listImage.size)
                })
                homeViewModel.refresherData()

            }
        } else {
            tv_internet.visibility = View.VISIBLE
        }
        // go to gallery screen
        fab_gallery.setOnClickListener(View.OnClickListener {
            val directions = HomeScreenDirections.actionHoneToGallery()
            NavHostFragment.findNavController(this@HomeScreen).navigate(directions)
        })
    }

    private fun init() {
        adapter = HomeAdapter(this)
        rv_images.layoutManager = GridLayoutManager(context , 2 , RecyclerView.VERTICAL , false)
        rv_images.setHasFixedSize(true)
        rv_images.adapter = adapter

        initScrollListener()
    }

    // item clicked
    override fun onClicked(position: Int , imageItemView: ImageItemView , imageView: ImageView , progressBar: ProgressBar) {
        CoroutineScope(Dispatchers.Main).launch {
            fab_gallery.isEnabled = false
            progressBar.visibility = View.VISIBLE
            if (context?.let { homeViewModel.saveImage(it , imageItemView.imageItem , position) } == null) {
                Toast.makeText(context , R.string.title_download_unsuccessful , Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context , R.string.title_download_successful , Toast.LENGTH_SHORT).show()
                homeViewModel.synchronizedData()
            }

            imageView.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
            fab_gallery.isEnabled = true
        }
    }

    // Scorll list and loadmore data
    private fun initScrollListener() {
        homeBinding.rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView , dx: Int , dy: Int) {
                super.onScrolled(recyclerView , dx , dy)

                val gridLayoutManager: GridLayoutManager = homeBinding.rvImages.layoutManager as GridLayoutManager
                if (statusLoadMore) {
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
        if (isNetworkConnected()) {
            statusLoadMore = false
            CoroutineScope(Dispatchers.Main).launch {
                fab_gallery.isEnabled = false
                progress_bar.visibility = View.VISIBLE

                homeViewModel.getData()
                delay(1500)

                progress_bar.visibility = View.INVISIBLE
                statusLoadMore = true
                fab_gallery.isEnabled = true
            }
        } else {
            Toast.makeText(context , R.string.title_notification , Toast.LENGTH_SHORT).show()
        }

    }

    private fun isNetworkConnected(): Boolean {
        val cm: ConnectivityManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo()!!.isConnected()
    }
}

