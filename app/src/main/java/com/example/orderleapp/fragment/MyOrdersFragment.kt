package com.example.orderleapp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orderleapp.api.MyOrdersApi
import com.example.orderleapp.apiResponse.MyOrdersApiResponse
import com.example.orderleapp.dataAdapter.MyOrdersDataAdapter
import com.example.orderleapp.databinding.FragmentMyOrdersBinding
import com.example.orderleapp.ui.MyOrders2Activity
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref


class MyOrdersFragment : Fragment() {
    private lateinit var binding: FragmentMyOrdersBinding
    private var model=java.util.ArrayList<MyOrdersApiResponse>()
    private lateinit var dataAdapter: MyOrdersDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyOrdersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.pgBar.visibility = View.VISIBLE
        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
        dataAdapter= MyOrdersDataAdapter(requireContext(),model)
        binding.recyclerView.adapter=dataAdapter

        addData()
        dataAdapter.onItemClick = {
            val intent = Intent(requireContext(), MyOrders2Activity::class.java)
            intent.putExtra("Data",it)
            startActivity(intent)
        }
    }

    private fun addData() {
        fetchProducts(requireContext())
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun fetchProducts(context: Context) {
        val myOrdersApi = MyOrdersApi(context) { ordersApiResponse ->

            for (order in ordersApiResponse) {
                model.add(order)
            }
            dataAdapter.notifyDataSetChanged()
            binding.pgBar.visibility = View.GONE

        }

        val userId = Pref.getValue(context, Config.PREF_USERID, "")
        val code = Pref.getValue(context, Config.PREF_CODE, "")
        val loginAccessToken = Pref.getValue(context, Config.PREF_LOGIN_ACCESS_TOKEN, "")

        myOrdersApi.getMyOrders(userId, code, loginAccessToken)

    }
}