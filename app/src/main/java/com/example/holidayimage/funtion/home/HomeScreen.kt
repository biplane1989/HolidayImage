package com.example.holidayimage.funtion.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreen : Fragment() , OnClicked {

    lateinit var homeViewModel: HomeViewModel
    lateinit var homeBinding: ActivityHomeScreenBinding
    private lateinit var adapter: HomeAdapter
    private var networkStatus = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel(activity!!.application)::class.java)
        adapter = HomeAdapter(this)

        loadMore()
        registerNetworkBroadcastForNougat()
        homeViewModel.getListImage()?.observe(this , Observer { listImage ->
            listImage?.let {
                adapter.submitList(ArrayList(listImage))
            }
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
        if (!homeViewModel.isNetworkConnected() && homeViewModel.getListSize() <= 0) {
            iv_error_internet?.let {
                iv_error_internet.visibility = View.VISIBLE
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                if (!homeViewModel.checkStatusServer() && homeViewModel.getListSize() <= 0) {
                    iv_error_server?.let { iv_error_server.visibility = View.VISIBLE }
                } else {
                    fab_gallery?.let { fab_gallery.visibility = View.VISIBLE }
                }
            }
        }
        // go to gallery screen
        fab_gallery.setOnClickListener(View.OnClickListener {
            val directions = HomeScreenDirections.actionHoneToGallery()
            NavHostFragment.findNavController(this@HomeScreen).navigate(directions)
        })
    }

    // item clicked
    override fun onClicked(position: Int , imageItemView: ImageItemView , imageView: ImageView , progressBar: ProgressBar) {
        if (homeViewModel.isNetworkConnected()) {
            CoroutineScope(Dispatchers.Main).launch {
                imageView.let { imageView.visibility = View.GONE }
                fab_gallery?.let { fab_gallery.isEnabled = false }
                fab_gallery?.let { fab_gallery.visibility = View.GONE }
                val imageItem = homeViewModel.startDownload(position)
                homeViewModel.downloading(imageItem)
                homeViewModel.downloaded(position)
                fab_gallery?.let { fab_gallery.isEnabled = true }
                fab_gallery?.let { fab_gallery.visibility = View.VISIBLE }
            }
        } else {
            Toast.makeText(context , R.string.title_notification , Toast.LENGTH_SHORT).show()
        }
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
        })
    }

    private fun loadMore() {
        if (homeViewModel.isNetworkConnected()) {
            CoroutineScope(Dispatchers.Main).launch {
                fab_gallery?.let { fab_gallery.isEnabled = false }
                fab_gallery?.let { fab_gallery.visibility = View.GONE }
                progress_bar?.let { progress_bar.visibility = View.VISIBLE }
                homeViewModel.loadMore()
                progress_bar?.let { progress_bar.visibility = View.GONE }
                fab_gallery?.let { fab_gallery.isEnabled = true }
                fab_gallery?.let { fab_gallery.visibility = View.VISIBLE }
            }
        } else {
            Toast.makeText(context , R.string.title_notification , Toast.LENGTH_SHORT).show()
            networkStatus = true
        }
    }

    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context? , intent: Intent?) {
            try {
                if (isOnline(context!!)) {
                    if (homeViewModel.getListSize() == 0 && networkStatus) {
                        iv_error_internet?.let { iv_error_internet.visibility = View.GONE }
                        loadMore()
                    }
                    homeViewModel.resetIsLoadMore()
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }

    private fun isOnline(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            //should check null because in airplane mode it will be null
            netInfo != null && netInfo.isConnected
        } catch (e: NullPointerException) {
            e.printStackTrace()
            false
        }
    }

    private fun registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context?.registerReceiver(networkChangeReceiver , IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context?.registerReceiver(networkChangeReceiver , IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    protected fun unregisterNetworkChanges() {
        try {
            context?.unregisterReceiver(networkChangeReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkChanges()
    }
}

