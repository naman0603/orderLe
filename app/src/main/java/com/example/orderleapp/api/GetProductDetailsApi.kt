package com.example.orderleapp.api


import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.orderleapp.apiResponse.ProductApiResponse
import com.example.orderleapp.dataModel.ProductDetailBean
import com.example.orderleapp.dataModel.ProductTypeBean
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class GetProductDetailsApiApi(
    private val context: Context,
    private val onSuccess: (ProductApiResponse) -> Unit,
) {

    fun getProductDetails(
        productId: Int,
        userId: String,
        code: String,
        loginAccessToken: String,
    ) {
        val url = Config.API_GET_PRODUCT_DETAILS

        val params = HashMap<String, String>()
        params["product_id"] = productId.toString()
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
                    val apiResponse = parseApiResponse(jsonResponse)
                    onSuccess(apiResponse!!)
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
    private fun parseApiResponse(jsonObj: JSONObject): ProductApiResponse? {
        try {
            val errorCode = jsonObj.optInt("error_code")
            val status = jsonObj.optBoolean("success")

            Log.d("parseApiResponse", "errorCode: $errorCode, status: $status")

            if (errorCode == 0 && status){
                val jsonObject = jsonObj.optJSONObject("data")

                if(jsonObject != null){
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
                        "",
                        ArrayList(), // Empty list of ProductTypeBean
                        ArrayList() // Empty list of ProductDetailBean
                    )
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

                    val jsonProductObject = jsonObject.optJSONObject("product_form_details")
                    jsonProductObject?.let {
                        Pref.setValue(context, Config.PREF_PRODUCT_CATEGORY, it.optString("product_category"))
                        Pref.setValue(context, Config.PREF_PRODUCT_GOLD_TYPE, it.optString("gold_type"))
                        Pref.setValue(context, Config.PREF_PRODUCT_CARAT_ID, it.optString("carat_id"))
                        Pref.setValue(context, Config.PREF_PRODUCT_NAME, it.optString("product_name"))
                        Pref.setValue(context, Config.PREF_PRODUCT_ITEM_CODE, it.optString("item_code"))
                        Pref.setValue(context, Config.PREF_PRODUCT_SORT_ORDER, it.optString("sort_order"))
                        Pref.setValue(context, Config.PREF_PRODUCT_PICTURE, it.optString("main_product_picture"))
                        Pref.setValue(context, Config.PREF_PRODUCT_WEIGHT, it.optString("product_weight"))
                        Pref.setValue(context, Config.PREF_PRODUCT_DESCRIPTION, it.optString("product_description"))
                        Pref.setValue(context, Config.PREF_PRODUCT_SHORT_DESCRIPTION, it.optString("short_description"))
                        Pref.setValue(context, Config.PREF_PRODUCT_SIZE, it.optString("has_size"))
                        Pref.setValue(context, Config.PREF_PRODUCT_WASTAGE, it.optString("wastage"))
                        Pref.setValue(context, Config.PREF_PRODUCT_STONE, it.optString("has_stone"))
                    }
                    val productjsonIdArray: JSONArray = jsonObject.getJSONArray("all_product_id")
                    val productDetailBeanArrayList = java.util.ArrayList<ProductDetailBean>()
                    for (i in 0 until productjsonIdArray.length()) {
                        val productJsonObject = productjsonIdArray.getJSONObject(i)
                        val productDetailBean = ProductDetailBean(
                            productJsonObject.optString("product_id")
                        )
                        productDetailBeanArrayList.add(productDetailBean)
                    }
                    productApiResponse.productDetailBeans = productDetailBeanArrayList

                    Log.d("productApiResponse",productApiResponse.toString())
                    return productApiResponse
                }
            }
        }catch (e:Exception){
            Log.e("Parsing Error", "Error parsing JSON response: ${e.message}")
        }
        return null
    }
}
