package com.example.orderleapp.api


import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.orderleapp.apiResponse.ProductApiResponse
import com.example.orderleapp.dataModel.ProductTypeBean
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref
import org.json.JSONException
import org.json.JSONObject

class GetProductApi(
    private val context: Context,
    private val onSuccess: (List<ProductApiResponse>) -> Unit,
) {

    fun getProductList(
        categoryId: Int,
        offset: Int,
        userId: String,
        code: String,
        loginAccessToken: String,
        caratId: Int,
    ) {
        val url = Config.API_GET_PRODUCT


        val params = HashMap<String, String>()
        params["category_id"] = categoryId.toString()
        params["offset"] = offset.toString()
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

        Log.d("parseApiResponse", "errorCode: $errorCode, status: $status")

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
                val jsonProductObject = jsonDataObject.optJSONObject("product_form_details")
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
            }
        }
        Log.d("parseApiResponse", "productBeanArrayList: $productBeanArrayList")
        return productBeanArrayList
    }
}
