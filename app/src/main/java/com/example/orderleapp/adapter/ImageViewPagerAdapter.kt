package com.example.orderleapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.orderleapp.R
import com.github.chrisbanes.photoview.PhotoView

class ImageViewPagerAdapter(private val context: Context, private val imageUrls: List<String>) : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.item_image_pager, container, false)

        // Find views within the item_image_pager.xml layout
        val photoView: PhotoView = itemView.findViewById(R.id.photoView)
        val btnPrevious: ImageView = itemView.findViewById(R.id.btnPrevious)
        val btnNext: ImageView = itemView.findViewById(R.id.btnNext)

        // Load image into the PhotoView using Glide
        Glide.with(context)
            .load(imageUrls[position])
            .into(photoView)
        photoView.scaleType = ImageView.ScaleType.FIT_CENTER

        // Update button visibility based on conditions
        if (imageUrls.size > 1) {
            btnPrevious.visibility =  View.VISIBLE
            btnNext.visibility =  View.VISIBLE
        } else {
            btnPrevious.visibility = View.GONE
            btnNext.visibility = View.GONE
        }

        btnPrevious.setOnClickListener {
            val newPosition = if (position > 0) (position - 1) else imageUrls.size - 1
            (container as ViewPager).currentItem = newPosition
        }

        btnNext.setOnClickListener {
            val newPosition = (position + 1) % imageUrls.size
            (container as ViewPager).currentItem = newPosition
        }
        container.addView(itemView)
        return itemView
    }    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }


    override fun getCount(): Int = imageUrls.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`
}
