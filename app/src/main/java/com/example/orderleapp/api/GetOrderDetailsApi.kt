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
import com.example.orderleapp.apiResponse.ProductApiResponse
import com.example.orderleapp.auth.LoginActivity
import com.example.orderleapp.dataModel.ProductDetailBean
import com.example.orderleapp.dataModel.ProductTypeBean
import com.example.orderleapp.`object`.Flags
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref
import org.json.JSONException
import org.json.JSONObject

class GetOrderDetailsApi(
    private val context: Context,
    private val onSuccess: (List<ProductApiResponse>) -> Unit,
) {

    fun getOrderDetails(
        requestMasterId: Int,
        userId: String,
        code: String,
        loginAccessToken: String,
    ) {
        val url = Config.API_GET_ORDER_DETAIL

        val params = HashMap<String, String>()
        params["request_master_id"] = requestMasterId.toString()
        params["user_id"] = userId
        params["code"] = code
        params["login_access_token"] = loginAccessToken
        Log.w("Params", params.toString())

        val request = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                try {
                    Log.d("::Response::", response)
                    val jsonResponse = JSONObject(response)
                    val apiResponseList = parseApiResponse(jsonResponse)
                    onSuccess(apiResponseList!!)
                } catch (e: JSONException) {
                    Log.e("Parsing Error", "Error parsing JSON response: ${e.message}")
                }
            },
            Response.ErrorListener { error ->
                Log.e("Product List Error", "API request error: ${error.message}")
            }
        ) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
    private fun parseApiResponse(jsonObj: JSONObject): List<ProductApiResponse>? {
        try {
            val productBeanArrayList: MutableList<ProductApiResponse> = mutableListOf()
            val errorCode = jsonObj.optInt("error_code")
            val status = jsonObj.optBoolean("success")

            Log.d("parseApiResponse", "errorCode: $errorCode, status: $status")

            if (errorCode == 0 && status){
                val jsonArray = jsonObj.optJSONArray("data")

                if (jsonArray != null) {
                    if (jsonArray.length() > 0) {
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val productApiResponse = ProductApiResponse(
                                0,
                                jsonObject.optInt("product_id", 0),
                                jsonObject.optInt("product_category_id", 0),
                                jsonObject.optString("product_name", ""),
                                jsonObject.optString("product_description", ""),
                                jsonObject.optString("product_picture_url", ""),
                                jsonObject.optString("product_picture", ""),
                                jsonObject.optDouble("product_weight", 0.0),
                                jsonObject.optString("created_date", ""),
                                jsonObject.optString("wastage", ""),
                                jsonObject.optString("stone_charge", "0"),
                                jsonObject.optInt("total_stone_charge", 0),
                                jsonObject.optString("category_title", ""),
                                jsonObject.optString("item_code", ""),
                                jsonObject.optInt("request_master_id", 0),
                                jsonObject.optInt("user_id", 0),
                                jsonObject.optString("request_number", ""),
                                jsonObject.optInt("product_request_id", 0),
                                jsonObject.optInt("quantity", 0),
                                jsonObject.optString("party_name", ""),
                                jsonObject.optString("party_email", ""),
                                jsonObject.optString("party_phone", ""),
                                jsonObject.optInt("order_status", 0),
                                jsonObject.optString("invoice_file", ""),
                                0,
                                jsonObject.optDouble("product_total_weight", 0.0),
                                jsonObject.optString("category_name", ""),
                                jsonObject.optString("size", ""),
                                false, // Set isChecked to false initially
                                emptyArray(), // Set ringSizeArray to null initially
                                jsonObject.optString("order_description", ""),
                                jsonObject.optString("gold_type", ""),
                                jsonObject.optInt("goldtype_id", 0),
                                "",
                                ArrayList<ProductTypeBean>(), // Empty list of ProductTypeBean
                                ArrayList<ProductDetailBean>() // Empty list of ProductDetailBean
                            )

                            Log.d("productApiResponse",productApiResponse.toString())
                            productBeanArrayList.add(productApiResponse)
                        }
                    }
                }
            }else if (errorCode == 4){
                popUp()
            }
            return productBeanArrayList
        }catch (e:Exception){
            Log.e("Parsing Error", "Error parsing JSON response: ${e.message}")
        }
        return null
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
