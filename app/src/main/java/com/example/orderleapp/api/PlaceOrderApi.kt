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
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.orderleapp.R
import com.example.orderleapp.auth.LoginActivity
import com.example.orderleapp.`object`.Flags
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref
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
        val errorCode = jsonObj.optInt("error_code")
        if(errorCode == 0) {
            return jsonObj.optBoolean("success", false)
        }else if (errorCode == 4){
            popUp()
            return false
        }else {
            return jsonObj.optBoolean("success", false)
        }
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
                context.finish()
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
