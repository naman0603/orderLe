package com.example.orderleapp.api

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.orderleapp.util.Config
import org.json.JSONException
import org.json.JSONObject

class LogoutApi(val context: Context) {
    fun logout(userId: String, code: String, callback: (Boolean) -> Unit) {
        val url = Config.API_LOGOUT_USER
        val params = HashMap<String, String>()

        params["user_id"] = userId
        params["code"] = code

        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                try {
                    Log.d("::Response::", "$response")
                    val jsonResponse = JSONObject(response)
                    val errorCode = jsonResponse.getInt("error_code")
                    val message = jsonResponse.getString("message")
                    val success = jsonResponse.getBoolean("success")

                    if (errorCode == 0 && success) {
                        callback(true) // Callback to indicate success
                    } else {
                        callback(false) // Callback to indicate failure
                    }
                } catch (e: JSONException) {
                    Log.e("Parsing Error", "Error parsing JSON response: ${e.message}")
                    callback(false) // Callback to indicate failure
                }
            },
            Response.ErrorListener { error ->
                Log.e("API ERROR", "API request error: ${error.message}")
                callback(false) // Callback to indicate failure
            }
        ) {
            override fun getParams(): Map<String, String> {
                Log.d("Params",params.toString())
                return params
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
}
