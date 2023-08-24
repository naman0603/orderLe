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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orderleapp.R
import com.example.orderleapp.api.GetProductApi
import com.example.orderleapp.apiResponse.CategoryApiResponse
import com.example.orderleapp.apiResponse.ProductApiResponse
import com.example.orderleapp.dataAdapter.ProductViewDataAdapter
import com.example.orderleapp.dataAdapter.ProductViewListDataAdapter
import com.example.orderleapp.databinding.ActivityProductViewBinding
import com.example.orderleapp.`interface`.CartCountObserver
import com.example.orderleapp.`object`.CartCountReceiverHolder
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref
import com.example.orderleapp.viewModel.DataHolder

@Suppress("DEPRECATION")
class ProductViewActivity : AppCompatActivity(),CartCountObserver {
    private lateinit var binding: ActivityProductViewBinding
    private var model=java.util.ArrayList<ProductApiResponse>()
    private var modelList=java.util.ArrayList<ProductApiResponse>()
    private lateinit var dataAdapter: ProductViewDataAdapter
    private lateinit var dataAdapterList : ProductViewListDataAdapter
    private var flag : Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CartCountReceiverHolder.register(this)
        val data = intent.getParcelableExtra<CategoryApiResponse>("Data")
        supportActionBar!!.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "${data?.categoryTitle}"
        }

        initView()
        onClicks()
    }

    override fun onDestroy() {
        super.onDestroy()
        CartCountReceiverHolder.unregister(this)
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun onClicks() {
        binding.imgGrid.setOnClickListener {
            binding.recyclerView.layoutManager= GridLayoutManager(this,2)
            dataAdapter= ProductViewDataAdapter(this,model)
            binding.recyclerView.adapter=dataAdapter
            model.clear()

            binding.relativeLayout.visibility = View.VISIBLE
            addData()

            dataAdapter.onItemClick = { productApiResponse ->
                DataHolder.setProductApiResponse(productApiResponse)
                startActivity(Intent(this, ProductViewActivity2::class.java))
            }
        }

        binding.imgList.setOnClickListener {
            binding.recyclerView.layoutManager= LinearLayoutManager(this)
            dataAdapterList= ProductViewListDataAdapter(this,modelList)
            binding.recyclerView.adapter=dataAdapterList
            modelList.clear()

            binding.relativeLayout.visibility = View.VISIBLE
            addDataList()

            dataAdapterList.onItemClick = { productApiResponse ->
                DataHolder.setProductApiResponse(productApiResponse)
                startActivity(Intent(this, ProductViewActivity2::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
    }
    @SuppressLint("InflateParams")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)

        val cartItem = menu.findItem(R.id.action_cart)
        val cartIcon = cartItem.icon

        // Inflate the custom badge layout
        val badgeLayout = layoutInflater.inflate(R.layout.menu_cart_action_layout, null)
        val badgeView = badgeLayout.findViewById<TextView>(R.id.badgeView)
        val badgeCount = fetchCount()
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

    private fun fetchCount(): Int {
        val sharedPreferences = getSharedPreferences("MyCartData", MODE_PRIVATE)
        return sharedPreferences.all.size
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_cart){
            startActivity(Intent(this,MyCartActivity::class.java))
            return true
        }else super.onOptionsItemSelected(item)

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {
        binding.relativeLayout.visibility = View.VISIBLE
        binding.recyclerView.layoutManager= GridLayoutManager(this,2)
        dataAdapterList= ProductViewListDataAdapter(this,modelList)
        dataAdapter= ProductViewDataAdapter(this,model)
        binding.recyclerView.adapter=dataAdapter

        addData()
        dataAdapter.onItemClick = { productApiResponse ->
            DataHolder.setProductApiResponse(productApiResponse)
            startActivity(Intent(this, ProductViewActivity2::class.java))
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addData() {
        flag = false
        fetchProducts(this)
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun addDataList() {
        flag = true
        fetchProducts(this)
    }
    @SuppressLint("SetTextI18n")
    fun fetchProducts(context: Context) {
        val data = intent.getParcelableExtra<CategoryApiResponse>("Data")
        val caratId = intent.getIntExtra("carat_id",0)


        val getProductApi = GetProductApi(context) { fetchedProductList ->

            if(fetchedProductList.isNotEmpty()){
                if (!flag){
                    setGrid(fetchedProductList)
                }else{
                    setList(fetchedProductList)
                }

            }else{
                Log.d("fetchedProductList","empty")
                binding.relativeLayout1.visibility = View.VISIBLE
                binding.txtEmpty.text = "No Products Available for ${data?.categoryTitle} !!"
            }

        }
        val categoryId = data?.categoryId
        val offset = 0 // Replace with the desired offset
        val userId = Pref.getValue(context, Config.PREF_USERID, "")
        val code = Pref.getValue(context, Config.PREF_CODE, "")
        val loginAccessToken = Pref.getValue(context, Config.PREF_LOGIN_ACCESS_TOKEN, "")

        getProductApi.getProductList(categoryId!!, offset, userId, code, loginAccessToken,caratId)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setList(fetchedProductList: List<ProductApiResponse>) {
        Log.d("fetchedProductList",fetchedProductList.toString())
        for (product in fetchedProductList) {
            // Extract all properties from the ProductApiResponse
            modelList.add( ProductApiResponse(
                product.id,
                product.productId,
                product.productCategoryId,
                product.productName,
                product.productDescription,
                product.productPictureUrl,
                product.productPicture,
                product.productWeight,
                product.createdDate,
                product.wastage,
                product.stoneCharge,
                product.totalStoneCharge,
                product.categoryTitle,
                product.itemCode,
                product.requestMasterId,
                product.userId,
                product.requestNumber,
                product.productRequestId,
                product.quantity,
                product.partyName,
                product.partyEmail,
                product.partyPhone,
                product.orderStatus,
                product.invoiceFile,
                product.qty,
                product.productTotalWeight,
                product.categoryName,
                product.ringSize,
                product.isChecked,
                product.ringSizeArray,
                product.orderDescription,
                product.goldType,
                product.goldTypeId,
                product.goldCarat,
                product.productTypeBeans,
                product.productDetailBeans
            ))
        }
        dataAdapterList.notifyDataSetChanged()
        binding.relativeLayout.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setGrid(fetchedProductList: List<ProductApiResponse>) {
        Log.d("fetchedProductList",fetchedProductList.toString())
        for (product in fetchedProductList) {
            // Extract all properties from the ProductApiResponse
            model.add( ProductApiResponse(
                product.id,
                product.productId,
                product.productCategoryId,
                product.productName,
                product.productDescription,
                product.productPictureUrl,
                product.productPicture,
                product.productWeight,
                product.createdDate,
                product.wastage,
                product.stoneCharge,
                product.totalStoneCharge,
                product.categoryTitle,
                product.itemCode,
                product.requestMasterId,
                product.userId,
                product.requestNumber,
                product.productRequestId,
                product.quantity,
                product.partyName,
                product.partyEmail,
                product.partyPhone,
                product.orderStatus,
                product.invoiceFile,
                product.qty,
                product.productTotalWeight,
                product.categoryName,
                product.ringSize,
                product.isChecked,
                product.ringSizeArray,
                product.orderDescription,
                product.goldType,
                product.goldTypeId,
                product.goldCarat,
                product.productTypeBeans,
                product.productDetailBeans
            ))
        }
        dataAdapter.notifyDataSetChanged()
        binding.relativeLayout.visibility = View.GONE

    }

    override fun onCartCountChanged() {
        invalidateOptionsMenu()
    }
}