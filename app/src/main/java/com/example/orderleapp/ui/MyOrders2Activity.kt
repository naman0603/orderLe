package com.example.orderleapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orderleapp.R
import com.example.orderleapp.api.GetOrderDetailsApi
import com.example.orderleapp.apiResponse.MyOrdersApiResponse
import com.example.orderleapp.apiResponse.ProductApiResponse
import com.example.orderleapp.dataAdapter.MyOrdersDetailsAdapter
import com.example.orderleapp.databinding.ActivityMyOrders2Binding
import com.example.orderleapp.`interface`.CartCountObserver
import com.example.orderleapp.`object`.CartCountReceiverHolder
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref

@Suppress("DEPRECATION")
class MyOrders2Activity : AppCompatActivity(),CartCountObserver {
    private lateinit var binding: ActivityMyOrders2Binding
    private var model=java.util.ArrayList<ProductApiResponse>()
    private lateinit var dataAdapter: MyOrdersDetailsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrders2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        CartCountReceiverHolder.register(this)
        val data = intent.getParcelableExtra<MyOrdersApiResponse>("Data")
        supportActionBar!!.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "${data?.requestNumber}"
        }
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        CartCountReceiverHolder.unregister(this)
    }
    @SuppressLint("InflateParams")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)

        val cartItem = menu.findItem(R.id.action_cart)
        val cartIcon = cartItem.icon
        val sharedPreferences = getSharedPreferences("MyCartData", MODE_PRIVATE)

        // Inflate the custom badge layout
        val badgeLayout = layoutInflater.inflate(R.layout.menu_cart_action_layout, null)
        val badgeView = badgeLayout.findViewById<TextView>(R.id.badgeView)
        val badgeCount = sharedPreferences.all.size

        // Set badge count and show/hide badge
        if (badgeCount > 0) {
            badgeView.visibility = View.VISIBLE
            badgeView.text = badgeCount.toString()
        } else {
            badgeView.visibility = View.GONE
        }

        // Create an ImageView for the cart icon
        val cartIconImageView = ImageView(this)
        cartIconImageView.setImageDrawable(cartIcon)

        cartIconImageView.setOnClickListener {
            // Handle the click event here (e.g., open cart activity)
            startActivity(Intent(this, MyCartActivity::class.java))
        }

        // Add the badge layout and cart icon to the FrameLayout
        val badgeFrameLayout = FrameLayout(this)
        badgeFrameLayout.addView(cartIconImageView)
        badgeFrameLayout.addView(badgeLayout)

        // Set the FrameLayout as the action view for the cart menu item
        cartItem.actionView = badgeFrameLayout

        return super.onCreateOptionsMenu(menu)
    }
    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_cart){
            startActivity(Intent(this,MyCartActivity::class.java))
            return true
        }else super.onOptionsItemSelected(item)

    }


    private fun initView() {
        binding.pgBar.visibility = View.VISIBLE
        binding.recyclerView.layoutManager= LinearLayoutManager(this)
        dataAdapter= MyOrdersDetailsAdapter(this,model)
        binding.recyclerView.adapter=dataAdapter
        addData()

    }

    private fun addData() {
        val data = intent.getParcelableExtra<MyOrdersApiResponse>("Data")
        fetchProducts(this,data!!.requestMasterId)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun fetchProducts(context: Context, masterId: Int) {

        val getOrderDetailsApi = GetOrderDetailsApi(context) { productBeanArrayList ->
            // Handle the productBeanArrayList here
            for (productApiResponse in productBeanArrayList) {
                model.add(productApiResponse)

                Log.d("Product Info", productApiResponse.productName) // Example usage
            }
            binding.txtOrderNo.text = "Order-$masterId"
            dataAdapter.notifyDataSetChanged()
            binding.pgBar.visibility = View.GONE

        }
        val userId = Pref.getValue(context, Config.PREF_USERID, "")
        val code = Pref.getValue(context, Config.PREF_CODE, "")
        val loginAccessToken = Pref.getValue(context, Config.PREF_LOGIN_ACCESS_TOKEN, "")

        getOrderDetailsApi.getOrderDetails(masterId, userId, code, loginAccessToken)
    }

    override fun onCartCountChanged() {
        invalidateOptionsMenu()
    }

}