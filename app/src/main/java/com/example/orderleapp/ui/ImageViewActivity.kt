package com.example.orderleapp.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.orderleapp.R
import com.example.orderleapp.databinding.ActivityImageViewBinding
import com.example.orderleapp.adapter.CustomViewPager
import com.example.orderleapp.adapter.ImageViewPagerAdapter

class ImageViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        setActionBarFun()
        if(intent.hasExtra("URIs")){
            val imageUrls: ArrayList<String> = intent.getStringArrayListExtra("URIs") ?: ArrayList()
            sendData(imageUrls)
        } else if(intent.hasExtra("URI")){
            val uri = intent.getStringExtra("URI")
            val imageUrls: ArrayList<String> = arrayListOf(uri.toString())
            sendData(imageUrls)
        } else {
            Toast.makeText(applicationContext, "No URLs detected", Toast.LENGTH_SHORT).show()
            binding.pgBar.visibility = View.GONE
        }
    }
    private fun setActionBarFun() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back_button)
            setCustomView(R.layout.custom_action_bar)
            val customActionBarView = customView
            val centerImage = customActionBarView.findViewById<ImageView>(R.id.center_image)
            val leftText = customActionBarView.findViewById<TextView>(R.id.left_text)
            centerImage.setImageResource(R.drawable.ic_woman)
        }
    }


    private fun sendData(imageUrls: ArrayList<String>) {
        val viewPager: CustomViewPager = binding.viewPager
        val pagerAdapter = ImageViewPagerAdapter(this, imageUrls)
        viewPager.adapter = pagerAdapter
        binding.pgBar.visibility = View.GONE
    }
}