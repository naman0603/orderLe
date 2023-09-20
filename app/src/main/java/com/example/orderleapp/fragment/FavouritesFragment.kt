package com.example.orderleapp.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderleapp.MyApplication
import com.example.orderleapp.R
import com.example.orderleapp.apiResponse.ProductApiResponse
import com.example.orderleapp.dataAdapter.FavouritesDataAdapter
import com.example.orderleapp.dataAdapter.ProductViewDataAdapter
import com.example.orderleapp.dataAdapter.ProductViewListDataAdapter
import com.example.orderleapp.databinding.FragmentFavouritesBinding
import com.example.orderleapp.ui.ProductViewActivity2
import com.example.orderleapp.viewModel.DataHolder


class FavouritesFragment : Fragment() {
    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var dataAdapter: FavouritesDataAdapter
    private var model=java.util.ArrayList<ProductApiResponse>()
    private lateinit var recyclerView:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouritesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerView()
        initView()
    }

    private fun registerView() {
        recyclerView = binding.recyclerView
    }

    private fun initView() {
        binding.recyclerView.layoutManager= GridLayoutManager(requireContext(),2)
        dataAdapter= FavouritesDataAdapter(requireContext(),model)
        binding.recyclerView.adapter=dataAdapter

        addData()
        dataAdapter.onItemClick = { productApiResponse ->
            DataHolder.setProductApiResponse(productApiResponse)
            startActivity(Intent(requireContext(), ProductViewActivity2::class.java))
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addData() {
        val dbHelper = MyApplication.databaseHelper
        val productApiResponses = dbHelper.getAllProductApiResponses()
        if(productApiResponses.isEmpty()){
            binding.txtEmpty.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        }
        for(productApiResponse in productApiResponses){
            model.add(productApiResponse)
        }
        dataAdapter.notifyDataSetChanged()
    }

}