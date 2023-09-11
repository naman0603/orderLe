package com.example.orderleapp.api

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.orderleapp.R
import com.example.orderleapp.apiResponse.MyOrdersApiResponse
import com.example.orderleapp.auth.LoginActivity
import com.example.orderleapp.`object`.Flags
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MyOrdersApi(private val context: Context, private val onSuccess: (List<MyOrdersApiResponse>) -> Unit) {

    fun getMyOrders(userId: String, code: String, loginAccessToken: String) {
        val url = Config.API_GET_MYORDER

        val params = HashMap<String, String>()
        params["user_id"] = userId
        params["code"] = code
        params["login_access_token"] = loginAccessToken
        Log.w("Params", params.toString())

        val request = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    Log.d("::Response::", "$response")
                    val jsonResponse = JSONObject(response)
                    val dataArray = jsonResponse.getJSONArray("data")
                    val errorCode = jsonResponse.getInt("error_code")
                    if(errorCode == 0 ){
                        val apiResponse = parseApiResponse(dataArray)
                        onSuccess(apiResponse)
                    }else if(errorCode == 4){
                        popUp()
                    }
                    val apiResponse = parseApiResponse(dataArray)
                    onSuccess(apiResponse)
                } catch (e: JSONException) {
                    Log.e("Parsing Error", "Error parsing JSON response: ${e.message}")
                }
            },
            Response.ErrorListener { error ->
                Log.e("My Orders Error", "API request error: ${error.message}")
            }
        ) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }

        Volley.newRequestQueue(context).add(request)
    }

    private fun parseApiResponse(jsonArray: JSONArray): List<MyOrdersApiResponse> {
        val ordersList: MutableList<MyOrdersApiResponse> = mutableListOf()

        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.getJSONObject(i)
            val id = jsonObj.optInt("id",0)
            val requestMasterId = jsonObj.optInt("request_master_id",0)
            val requestNumber = jsonObj.optString("request_number","")
            val createdDate = jsonObj.optString("created_date","")
            val categoryId = jsonObj.optInt("category_id",0)
            val categoryTitle = jsonObj.optString("category_title","")
            val categoryPictureUrl = jsonObj.optString("category_picture_url","")
            val categoryPicture = jsonObj.optString("category_picture","")

            val orderApiResponse = MyOrdersApiResponse(
                id, requestMasterId, requestNumber, createdDate, categoryId,
                categoryTitle, categoryPictureUrl, categoryPicture
            )

            ordersList.add(orderApiResponse)
        }
        Log.d("ordersList", ordersList.toString())
        return ordersList
    }
    private fun popUp() {
        val dialogBinding = LayoutInflater.from(context).inflate(R.layout.card_view_user_exist,null)
        val builder = Dialog(context)

        val userName =  Pref.getValue(context, Config.PREF_USERNAME, "")
        builder.setContentView(dialogBinding)
        if(userName!=""){

            val txtName : TextView = builder.findViewById(R.id.txtName)
            txtName.visibility = View.VISIBLE
            txtName.text = userName

        }
        val logOut : TextView = builder.findViewById(R.id.txtOk)
        logOut.setOnClickListener {
            Flags.init(context)
            context.startActivity(Intent(context, LoginActivity::class.java))
            if (context is Activity) {
                context.finish() // Finish the activity if the context is an instance of Activity
            }
            Flags.myFlag = false
            builder.dismiss()
        }

        builder.setContentView(dialogBinding)
        builder.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.popup_sell_bg
            )
        )
        builder.show()
    }

}
