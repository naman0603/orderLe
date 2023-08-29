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
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.orderleapp.R
import com.example.orderleapp.apiResponse.ProductApiResponse
import com.example.orderleapp.auth.LoginActivity
import com.example.orderleapp.dataModel.ProductTypeBean
import com.example.orderleapp.`object`.Flags
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref
import org.json.JSONException
import org.json.JSONObject

class GoldTypeFilterApi(
    private val context: Context,
    private val onSuccess: (List<ProductApiResponse>) -> Unit,
) {

    fun goldTypeFilterApi(
        categoryId: Int,
        goldTypeId: Int,
        userId: String,
        code: String,
        loginAccessToken: String,
        caratId: Int,
    ) {
        val url = Config.API_GOLD_TYPE_FILTER

        val params = HashMap<String, String>()
        params["category_id"] = categoryId.toString()
        params["gold_type_id"] = goldTypeId.toString()
        params["user_id"] = userId
        params["code"] = code
        params["login_access_token"] = loginAccessToken
        Log.w("Params", params.toString())

        val request = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                try {
                    Log.d("::Response::", response)
                    val jsonResponse = JSONObject(response)
                    val apiResponse = parseApiResponse(jsonResponse,caratId)
                    onSuccess(apiResponse)
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
    private fun parseApiResponse(jsonObj: JSONObject, caratId: Int): List<ProductApiResponse> {
        Log.d("parseApiResponse", "Parsing JSON: $jsonObj")

        var goldCarat = ""
        if (caratId == 1) {
            goldCarat = "18K"
        } else if (caratId == 2) {
            goldCarat = "22K"
        }
        val productBeanArrayList: MutableList<ProductApiResponse> = mutableListOf()

        val errorCode = jsonObj.optInt("error_code")
        val status = jsonObj.optBoolean("success")
        val mesg = jsonObj.optString("message")

        Log.d("parseApiResponse", "errorCode: $errorCode, status: $status, message :$mesg")

        if (errorCode == 0 && status) {
            val jsonDataObject = jsonObj.optJSONObject("data")
            if (jsonDataObject != null && jsonDataObject.has("products")) {
                val jsonArray = jsonDataObject.getJSONArray("products")

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
                            jsonObject.optString("ringSize", ""),
                            false, // Set isChecked to false initially
                            emptyArray(), // Set ringSizeArray to null initially
                            jsonObject.optString("order_description", ""),
                            jsonObject.optString("gold_type", ""),
                            jsonObject.optInt("goldtype_id", 0),
                            goldCarat,
                            ArrayList(), // Empty list of ProductTypeBean
                            ArrayList() // Empty list of ProductDetailBean
                        )
                        Log.d("productBeanArrayList",productApiResponse.productId.toString())
                        if (jsonObject.has("product_images")) {
                            val productTypeBeanArrayList = ArrayList<ProductTypeBean>()
                            val productjsonArray = jsonObject.getJSONArray("product_images")

                            for (k in 0 until productjsonArray.length()) {
                                val productJsonObject = productjsonArray.getJSONObject(k)

                                val productTypeBean = ProductTypeBean(
                                    productJsonObject.optString("product_image", ""),
                                    productJsonObject.optInt("media_type", 0),
                                    productJsonObject.optString("video_frame", "")
                                )
                                productTypeBeanArrayList.add(productTypeBean)
                            }
                            productApiResponse.productTypeBeans = productTypeBeanArrayList
                        }

                        if (jsonObject.has("size")) {
                            val sizejsonArray = jsonObject.getJSONArray("size")
                            val list = mutableListOf<CharSequence>()
                            for (j in 0 until sizejsonArray.length()) {
                                list.add(sizejsonArray.getString(j))
                            }
                            productApiResponse.ringSizeArray = list.toTypedArray()
                        }
                        productBeanArrayList.add(productApiResponse)
                    }
                }
            }
        }else if (errorCode == 4){
            popUp()
        }
        Log.d("parseApiResponse", "productBeanArrayList: $productBeanArrayList")
        return productBeanArrayList
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
        val logOut :TextView = builder.findViewById(R.id.txtOk)
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
