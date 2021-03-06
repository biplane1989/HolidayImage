package com.example.holidayimage.funtion.detail

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.holidayimage.R
import com.example.holidayimage.databinding.ActivityDetailScreenBinding
import kotlinx.android.synthetic.main.activity_detail_screen.*


class DetailScreen : Fragment() {

    private var url: String = ""
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var detailBinding: ActivityDetailScreenBinding

    override fun onCreateView(inflater: LayoutInflater , container: ViewGroup? , savedInstanceState: Bundle?): View? {

        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        detailBinding = DataBindingUtil.inflate(layoutInflater , R.layout.activity_detail_screen , container , false)
        detailBinding.lifecycleOwner = this
        detailBinding.detailviewmodel = detailViewModel
        return detailBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        url = arguments?.let { DetailScreenArgs.fromBundle(it).url }!!

        detailViewModel.getImageById(url)

        detailViewModel.getImage().observe(viewLifecycleOwner , Observer { image ->
            Glide.with(this).load(image.path).into(iv_detail)
        })

        fab_delete.setOnClickListener(View.OnClickListener {
            checkDeletedialog()
        })
    }

    private fun checkDeletedialog() {
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it) }
        alertDialogBuilder?.setMessage(R.string.messgess_dialog)
        alertDialogBuilder?.setPositiveButton(R.string.yes , object : DialogInterface.OnClickListener {
            override fun onClick(arg0: DialogInterface? , arg1: Int) {

                detailViewModel.deleteImage()
                iv_detail.visibility = View.GONE
                tv_notification.visibility = View.VISIBLE
                fab_delete.visibility = View.GONE
            }
        })

        alertDialogBuilder?.setNegativeButton(R.string.no , object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface? , which: Int) {
            }
        })
        val alertDialog = alertDialogBuilder?.create()
        alertDialog?.show()
    }
}

