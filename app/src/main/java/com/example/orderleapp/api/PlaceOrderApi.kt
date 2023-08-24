package com.example.orderleapp.api

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.orderleapp.util.Config
import org.json.JSONException
import org.json.JSONObject

class PlaceOrderApi(
    private val context: Context,
    private val onSuccess: (Boolean) -> Unit,
) {

    fun placeOrder(
        userId: String,
        code: String,
        product: String,
        orderDescription: String,
        loginAccessToken: String,
    ) {
        val url = Config.API_REQUEST_PRODUCT
        Log.d("API_REQUEST_PRODUCT",url)

        val params = HashMap<String, String>()
        params["user_id"] = userId
        params["code"] = code
        params["product"] = product
        params["order_description"] = orderDescription
        params["login_access_token"] = loginAccessToken
        Log.w("PlaceOrderParams", params.toString())

        val request = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                try {
                    Log.d("::PlaceOrderResponse::", "$response")
                    val jsonResponse = JSONObject(response)
                    val success = parseApiResponse(jsonResponse)
                    onSuccess(success)
                } catch (e: JSONException) {
                    Log.e("Parsing Error", "Error parsing JSON response: ${e.message}")
                }
            },
            Response.ErrorListener { error ->
                Log.e("Place Order Error", "API request error: ${error.toString()}")
            }
        ) {
            override fun getParams(): Map<String, String> {
                return params
            }

        }
        val retryPolicy = DefaultRetryPolicy(
            30000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        request.retryPolicy = retryPolicy

        Volley.newRequestQueue(context).add(request)
    }

    private fun parseApiResponse(jsonObj: JSONObject): Boolean {
        return jsonObj.optBoolean("success", false)
    }
}
