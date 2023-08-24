package com.example.orderleapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.orderleapp.databinding.ActivityImageViewBinding
import com.example.orderleapp.adapter.CustomViewPager
import com.example.orderleapp.adapter.ImageViewPagerAdapter

class ImageViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
        initView()
    }

    private fun initView() {
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

    private fun sendData(imageUrls: ArrayList<String>) {
        val viewPager: CustomViewPager = binding.viewPager
        val pagerAdapter = ImageViewPagerAdapter(this, imageUrls)
        viewPager.adapter = pagerAdapter
        binding.pgBar.visibility = View.GONE
    }
}