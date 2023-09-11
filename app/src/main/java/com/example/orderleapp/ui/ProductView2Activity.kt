package com.example.orderleapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.orderleapp.R
import com.example.orderleapp.adapter.CustomViewPager
import com.example.orderleapp.adapter.ImageViewPagerAdapter
import com.example.orderleapp.api.GetProductDetailsApiApi
import com.example.orderleapp.apiResponse.ProductApiResponse
import com.example.orderleapp.dataModel.MyCartDataModel
import com.example.orderleapp.databinding.ActivityProductView2Binding
import com.example.orderleapp.`interface`.CartCountObserver
import com.example.orderleapp.`object`.CartCountReceiverHolder
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref
import com.example.orderleapp.viewModel.DataHolder
import com.google.gson.Gson

class ProductViewActivity2 : AppCompatActivity(),CartCountObserver {
    private lateinit var binding: ActivityProductView2Binding
    private lateinit var weight:String
    private var counter:Int = 0
    var selectedProduct : ProductApiResponse? = null
    var url : String = ""
    private lateinit var progressView: View
    private var imgUrls = ArrayList<String>()
    private var backBtn : Int = 0

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductView2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        CartCountReceiverHolder.register(this)


        progressView = layoutInflater.inflate(R.layout.layout_custom_progress, null)
        progressView.visibility = View.GONE
        binding.root.addView(progressView)

        initView()
        onClickListeners()

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
    @SuppressLint("SetTextI18n")
    private fun onClickListeners() {

        binding.add.setOnClickListener {
           val  counter = binding.txtCounter.text.toString().toInt()

            val change = counter + 1

            binding.txtCounter.text = change.toString()
            binding.txtWeight.text = (weight.toFloat()*change).toString()+" gms"

        }

        binding.sub.setOnClickListener {
            val counter = binding.txtCounter.text.toString().toInt()
            val flag = 1

            if(counter == flag){
                binding.txtCounter.text = "1"
                binding.txtWeight.text = (weight.toFloat()).toString()+" gms"

            }else{
                val change = counter - 1
                binding.txtCounter.text = change.toString()
                binding.txtWeight.text = (weight.toFloat()*change).toString()+" gms"
            }
        }

        binding.btnAddToCart.setOnClickListener {
            counter = binding.txtCounter.text.toString().toInt()
            if (counter>0){
                saveToCart(counter)
                binding.txtCounter.text = "1"
                binding.txtWeight.text = (weight.toFloat()).toString()+" gms"
            }else{
                Toast.makeText(this, "Please Enter Proper Quantity", Toast.LENGTH_SHORT).show()
            }
        }
        binding.imgView.setOnClickListener {
            sendURL(url)
        }
    }

    private fun saveToCart(counter: Int) {
        val sharedPreferences = getSharedPreferences("MyCartData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val itemKey = selectedProduct!!.productId.toString()
        val existingCartDataJson = sharedPreferences.getString(itemKey, null)

        if (existingCartDataJson != null) {
            // If item already exists, update its quantity
            val existingCartDataModel = Gson().fromJson(existingCartDataJson, MyCartDataModel::class.java)
            val updatedCounter = existingCartDataModel.counter.toInt() + counter
            existingCartDataModel.counter = updatedCounter.toString()

            val updatedCartDataJson = Gson().toJson(existingCartDataModel)
            editor.putString(itemKey, updatedCartDataJson)
            editor.apply()

            Toast.makeText(this, "Quantity updated in cart", Toast.LENGTH_SHORT).show()
        } else {
            // If item does not exist, add it to the cart
            val cartDataModel = MyCartDataModel(
                selectedProduct!!.productId.toString(),
                selectedProduct!!.productPictureUrl + selectedProduct!!.productPicture,
                selectedProduct!!.productName,
                selectedProduct!!.stoneCharge,
                selectedProduct!!.productWeight.toString(),
                counter.toString(),
                selectedProduct!!.categoryName,
                selectedProduct!!.ringSize
            )

            val gson = Gson()
            val cartDataJson = gson.toJson(cartDataModel)

            editor.putString(itemKey, cartDataJson)
            editor.apply()
            CartCountReceiverHolder.sendCartCountChangedBroadcast(this)

        }
    }

    private fun initView() {
        setActionBarFun()
        progressView.visibility = View.VISIBLE
        selectedProduct = DataHolder.getProductApiResponse()
        fetchProducts(this)
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
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

            centerImage.setImageResource(R.drawable.ic_woman)

        }
    }


    @SuppressLint("SetTextI18n")
    private fun fetchProducts(context: Context) {

        val getProductDetailsApi = GetProductDetailsApiApi(context) {
            binding.txtClientName.text = it.productName
            binding.txtCategory.text = it.categoryName
            binding.txtCharges.text = it.stoneCharge
            if(it.productDescription != null && it.productDescription!="null"){
                binding.txtDescription.text = it.productDescription
            }
            binding.txtWeight.text = it.productWeight.toString()+" gms"

            weight = it.productWeight.toString()
            imageSlider(it)
        }
        progressView.visibility = View.GONE

        val productId = selectedProduct!!.productId
        val userId = Pref.getValue(context, Config.PREF_USERID, "")
        val code = Pref.getValue(context, Config.PREF_CODE, "")
        val loginAccessToken = Pref.getValue(context, Config.PREF_LOGIN_ACCESS_TOKEN, "")

        getProductDetailsApi.getProductDetails(productId, userId, code, loginAccessToken)
    }

    private fun imageSlider(list: ProductApiResponse) {
        val imageUrls = ArrayList<String>()
        if (list.productTypeBeans.size > 0) {
            imageUrls.add(list.productPictureUrl + list.productPicture)
            for (productTypeBean in list.productTypeBeans) {
                imageUrls.add(list.productPictureUrl + productTypeBean.productImage)
            }
        } else {
            url = list.productPictureUrl + list.productPicture
            imageUrls.add(url)
        }
        imgUrls = imageUrls

        val slider = binding.imageView
        if(imgUrls.size > 1){
            setupImageSlider(slider, imageUrls)
        }else{
            setUpImage()
        }
    }

    private fun setUpImage() {
        Glide.with(this).load(url).into(binding.imgView)
    }

    private fun setupImageSlider(slider: CustomViewPager, bannerList: ArrayList<String>) {
        binding.imgView.visibility = View.GONE
        slider.visibility = View.VISIBLE

        val pagerAdapter = ImageViewPagerAdapter(this, bannerList)
        slider.adapter = pagerAdapter
    }

    private fun sendURL(uri: String?) {
        val intent = Intent(this, ImageViewActivity::class.java)
        intent.putExtra("URI", uri)
        startActivity(intent)
    }

    override fun onCartCountChanged() {
        invalidateOptionsMenu()
    }

    override fun onBackPressed() {
        if (backBtn >= 1) {
            // If the back button is pressed more than once, close the application.
            finishAffinity()
        } else {
            // If the back button is pressed for the first time, show a toast message.
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            backBtn++

            // Reset the count after a certain delay (e.g., 2 seconds).
            Handler(Looper.getMainLooper()).postDelayed({
                backBtn = 0
            }, 4000)
        }
    }

}