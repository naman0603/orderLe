package com.example.orderleapp.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.orderleapp.api.BannerListApi
import com.example.orderleapp.api.GetAllCategoryApi
import com.example.orderleapp.apiResponse.CategoryApiResponse
import com.example.orderleapp.dataAdapter.DashboardDataAdapter
import com.example.orderleapp.dataModel.DashboardDataModel
import com.example.orderleapp.databinding.FragmentDashboardBinding
import com.example.orderleapp.ui.ImageViewActivity
import com.example.orderleapp.ui.ProductViewActivity
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref


class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private var model=java.util.ArrayList<CategoryApiResponse>()
    private lateinit var dataAdapter: DashboardDataAdapter
    private var isProgressBarVisible = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        imageSlider()
        recyclerView()
    }

    private fun recyclerView() {
        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
        dataAdapter= DashboardDataAdapter(requireContext(),model)
        binding.recyclerView.adapter=dataAdapter
        addData()

        dataAdapter.onItemClick = {
            val intent = Intent(requireContext(), ProductViewActivity::class.java)
            intent.putExtra("Data",it)
            intent.putExtra("carat_id",1)
            startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addData() {
        toggleProgressBarVisibility(true)
        // Inside your activity or fragment
        val getAllCategoryApi = GetAllCategoryApi(requireContext()) { categoryList ->
            // Handle the retrieved categoryList here
            for (category in categoryList) {
                // Access category properties
                val id = category.id
                val requestMasterId = category.requestMasterId
                val requestNumber = category.requestNumber
                val createdDate = category.createdDate
                val categoryId = category.categoryId
                val categoryTitle = category.categoryTitle
                val categoryPictureUrl = category.categoryPictureUrl
                val categoryPicture = category.categoryPicture
                model.add(
                    CategoryApiResponse(
                    id, requestMasterId, requestNumber, createdDate, categoryId, categoryTitle, categoryPictureUrl, categoryPicture
                )
                )
            }
            dataAdapter.notifyDataSetChanged()
            toggleProgressBarVisibility(false)
        }


        val userId = Pref.getValue(context, Config.PREF_USERID, "")
        val code = Pref.getValue(context, Config.PREF_CODE, "")
        val caratId = "1"
        val loginAccessToken = Pref.getValue(context, Config.PREF_LOGIN_ACCESS_TOKEN, "")

        getAllCategoryApi.getCategoryList(userId, code, caratId, loginAccessToken)
    }


    private fun imageSlider() {
        toggleProgressBarVisibility(true)
        val slider = binding.imgSlider
        val list = ArrayList<SlideModel>()

        val bannerListApi = BannerListApi(requireContext()) { parsedBannerList ->
            // Handle the parsed banner list here
            for (banner in parsedBannerList) {
                list.add(SlideModel(banner.banner))
                Log.d("ImageUri",banner.banner)
            }
            Log.d("ImageUri",list.toString())
            // After adding banners to the list, set up the image slider
            setupImageSlider(slider, list)
        }

        val userId = Pref.getValue(requireContext(), Config.PREF_USERID, "").toString()
        val code = Pref.getValue(requireContext(), Config.PREF_CODE, "")
        val loginAccessToken = Pref.getValue(requireContext(), Config.PREF_LOGIN_ACCESS_TOKEN, "")

        bannerListApi.getBannerList(userId, code, loginAccessToken)
    }

    private fun setupImageSlider(slider: ImageSlider, bannerList: List<SlideModel>) {

        slider.setImageList(bannerList, ScaleTypes.FIT)
        toggleProgressBarVisibility(false)
        slider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                val imageURI = bannerList[position].imageUrl
                sendURL(imageURI)
            }
        })
    }
    private fun sendURL(uri: String?) {
        val intent = Intent(requireContext(), ImageViewActivity::class.java)
        intent.putExtra("URI", uri)
        startActivity(intent)
    }

    private fun toggleProgressBarVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.transparentProgressBar.visibility = View.VISIBLE
            binding.root.setOnTouchListener { _, _ -> true } // Disable touch
        } else {
            binding.transparentProgressBar.visibility = View.GONE
            binding.root.setOnTouchListener(null) // Enable touch
        }
        isProgressBarVisible = isVisible
    }

}