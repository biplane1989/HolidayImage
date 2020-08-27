package com.example.holidayimage.funtion.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.holidayimage.R
import com.example.holidayimage.`object`.ImageFile
import com.example.holidayimage.databinding.ActivityGalleryScreenBinding
import com.example.holidayimage.funtion.home.HomeScreenDirections
import kotlinx.android.synthetic.main.activity_gallery_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryScreen : Fragment() , OnGalleryClicked {

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var galleryBinding: ActivityGalleryScreenBinding
    private lateinit var adapter: GalleryAdapter
    private var positition: Int = -1

    override fun onCreateView(inflater: LayoutInflater , container: ViewGroup? , savedInstanceState: Bundle?): View? {

        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)

        galleryBinding = DataBindingUtil.inflate(
            layoutInflater , R.layout.activity_gallery_screen , container , false
        )
        galleryBinding.lifecycleOwner = this
        galleryBinding.galleryviewmodel = galleryViewModel
        return galleryBinding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        init()
        galleryViewModel.getListImage().observe(viewLifecycleOwner , Observer { listImage ->
            CoroutineScope(Dispatchers.Main).launch {
                adapter.submitList(ArrayList(listImage))
                rv_gallery.layoutManager?.scrollToPosition(positition - 1)
            }
        })


    }

    override fun onStart() {
        super.onStart()
        galleryViewModel.initData()
    }

    private fun init() {
        adapter = GalleryAdapter(this)
        rv_gallery.layoutManager = GridLayoutManager(context , 2 , RecyclerView.VERTICAL , false)
        rv_gallery.setHasFixedSize(true)
        rv_gallery.adapter = adapter

    }

    override fun onClick(imageFile: ImageFile , positition: Int) {
        this.positition = positition
        val directions = GalleryScreenDirections.actionGalleryToDetail().setUrl(imageFile.url)
        NavHostFragment.findNavController(this).navigate(directions)
    }

}