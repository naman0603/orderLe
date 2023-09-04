@file:Suppress("DEPRECATION")

package com.example.orderleapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orderleapp.R
import com.example.orderleapp.api.PlaceOrderApi
import com.example.orderleapp.dataAdapter.MyCartDataAdapter
import com.example.orderleapp.dataModel.MyCartDataModel
import com.example.orderleapp.databinding.ActivityMyCartBinding
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener

class MyCartActivity : AppCompatActivity(),PaymentResultListener {

    lateinit var binding: ActivityMyCartBinding
    private val model = HashMap<String, MyCartDataModel>()
    private lateinit var dataAdapter: MyCartDataAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var quantity : Int = 0
    private var token : String = ""
    lateinit var sb: StringBuilder
    private lateinit var progressView: View
    private var backBtn : Int = 0


    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)


        tokenCollection()
        initView()
        progressView = layoutInflater.inflate(R.layout.layout_custom_progress, null)
        progressView.visibility = View.GONE // Hide it initially
        binding.root.addView(progressView)
    }

    private fun tokenCollection() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                token = task.result
                Log.d("TOKEN_SUCCESSFUL", "FCM token: $token")
            } else {
                Log.e("TOKEN_UNSUCCESSFUL", "Failed to get FCM token")
            }
        }
    }

    private fun initView() {
        setActionBarFun()
        sharedPreferences = getSharedPreferences("MyCartData", MODE_PRIVATE)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        dataAdapter = MyCartDataAdapter(this, model)
        binding.recyclerView.adapter = dataAdapter

        addData()
        if (model.isEmpty()){
            popUpEmpty()
        }
        binding.btnPurchase.setOnClickListener {
            if (model.isNotEmpty()) {
                popUp()
                Checkout.preload(this)
            }
            else {
                Toast.makeText(this, "Please Add Items to the Cart", Toast.LENGTH_SHORT).show()
            }
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

    private fun popUpEmpty() {
        val builder = AlertDialog.Builder(this,R.style.AlertDialogTheme)
        //set title for alert dialog
        builder.setTitle("Alert")
        //set message for alert dialog
        builder.setMessage("Please Enter Data Into Cart ")
        builder.setIcon(R.drawable.ic_alert_box)
        //performing neutral action
        builder.setNeutralButton("Ohk"){ _, _ ->}
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun popUp() {
        val builder = AlertDialog.Builder(this,R.style.AlertDialogTheme)
        //set title for alert dialog
        builder.setTitle("Confirm")
        //set message for alert dialog
        val message = "Confirm Your Purchase"
        builder.setMessage(Html.fromHtml(message))
        builder.setIcon(R.drawable.ic_thumb_up)
        //performing positive action
        builder.setPositiveButton("Confirm"){ _, _ ->
            progressView.visibility = View.VISIBLE
            placeOrderApi()
        }
        //performing negative action
        builder.setNegativeButton("Deny"){ _, _ ->}
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun placeOrderApi() {
        val cartDataList = ArrayList<MyCartDataModel>()

        val gson = Gson()
        val allEntries = sharedPreferences.all
        for ((_, value) in allEntries) {
            val cartDataJson = value as? String
            if (cartDataJson != null) {
                val myCartDataModel = gson.fromJson(cartDataJson, MyCartDataModel::class.java)
                cartDataList.add(myCartDataModel)
            }
        }

        sb = StringBuilder()
        if (cartDataList.isNotEmpty()) {
            sb.append("[")
            for (i in cartDataList.indices) {
                sb.append("{\"product_id\":\"" + cartDataList[i].id + "\",")
                sb.append("\"quantity\":\"" + cartDataList[i].counter + "\",")
                sb.append("\"product_name\":\"" + cartDataList[i].name + "\",")
                sb.append("\"category_title\":\"" + cartDataList[i].categoryName + "\",")
                sb.append("\"size\":\"" + cartDataList[i].ringSize + "\"}")
                sb.append(if (i + 1 < cartDataList.size) "," else "")
            }
            sb.append("]")
        }
        val placeOrderApi = PlaceOrderApi(this) { success ->
            if (success) {
                progressView.visibility = View.GONE
                Toast.makeText(this, "Order Successfully Placed", Toast.LENGTH_SHORT).show()
                model.clear()
                dataAdapter.notifyDataSetChanged()
                val sharedPreferences = getSharedPreferences("MyCartData", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()
                sendBroadcast(Intent("CART_UPDATED"))
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }

        val userId = Pref.getValue(this, Config.PREF_USERID, "")
        val code = Pref.getValue(this, Config.PREF_CODE, "")
        val loginAccessToken = Pref.getValue(this, Config.PREF_LOGIN_ACCESS_TOKEN, "")
        val product = sb.trim().toString()
        val orderDescription = ""

        placeOrderApi.placeOrder(userId, code, product, orderDescription, loginAccessToken)

    }

/*
    private fun payments(amount:Int){
        val checkOut = Checkout()
        checkOut.setKeyID("rzp_test_Zaw0kVOalIpz1n")
        try {
            val options = JSONObject()
            options.put("name","OrderLe")
            options.put("theme.color","#e8caca")
            options.put("currency","INR")
            options.put("amount",amount * 100)

            val retryObj = JSONObject()
            retryObj.put("enabled",true)
            retryObj.put("max_count",4)

            options.put("retry",retryObj)

            checkOut.open(this,options)
        }catch (e : Exception){
            Log.v("PaymentError",e.message.toString())
            Toast.makeText(this, "Error :"+e.message, Toast.LENGTH_LONG).show()
        }
    }
*/

    @SuppressLint("NotifyDataSetChanged")
    private fun addData() {
        val gson = Gson()
        val allEntries = sharedPreferences.all
        for ((key, value) in allEntries) {
            val cartDataJson = value as? String
            if (cartDataJson != null) {
                val myCartDataModel = gson.fromJson(cartDataJson, MyCartDataModel::class.java)
                model[key] = myCartDataModel
            }
        }
        dataAdapter.notifyDataSetChanged()
        binding.progressBar.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Thank You For Purchasing $quantity items using OrderLe ", Toast.LENGTH_SHORT).show()
        model.clear()
        val sharedPreferences = getSharedPreferences("MyCartData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        dataAdapter.notifyDataSetChanged()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Unsuccessful.\nPlease Try Again Later ", Toast.LENGTH_SHORT).show()
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