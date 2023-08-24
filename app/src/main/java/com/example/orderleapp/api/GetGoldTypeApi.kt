package com.example.orderleapp.api

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.orderleapp.apiResponse.ProductApiResponse
import com.example.orderleapp.util.Config
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class GetGoldTypeApi(
    private val context: Context,
    private val onSuccess: (List<ProductApiResponse>) -> Unit,
) {

    fun getProductList(
        userId: String,
        code: String,
        loginAccessToken: String,
    ) {
        val url = Config.API_GET_GOLD_TYPE

        val params = HashMap<String, String>()
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
                    val dataArray = jsonResponse.getJSONArray("data")
                    val apiResponse = parseApiResponse(dataArray)
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

    private fun parseApiResponse(jsonArray: JSONArray): List<ProductApiResponse> {
        val productList: MutableList<ProductApiResponse> = mutableListOf()

        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.getJSONObject(i)

            val productApiResponse = Gson().fromJson(jsonObj.toString(), ProductApiResponse::class.java)

            val goldType = jsonObj.getString("gold_type") // Retrieve the gold type field
            val goldTypeId = jsonObj.getInt("goldtype_id") // Retrieve the gold type ID field

            // Set the goldType and goldTypeId directly in the parsed instance
            productApiResponse.goldType = goldType
            productApiResponse.goldTypeId = goldTypeId

            productList.add(productApiResponse)
        }
        Log.d("productList", productList.toString())
        return productList
    }
}
