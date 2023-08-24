package com.example.orderleapp.api

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.orderleapp.util.Config
import org.json.JSONException
import org.json.JSONObject

class ChangePasswordApi(val context: Context,val  callback: (Boolean) -> Unit) {
    fun changePassword(
        userId: String,
        oldPassword: String,
        newPassword: String,
        confirmPassword: String,
        code: String,
        loginAccessToken: String
    ) {
        val url = Config.API_UPDATE_PASSWORD
        val params = HashMap<String, String>()

        params["user_id"] = userId
        params["old_password"] = oldPassword
        params["password"] = newPassword
        params["confirm_password"] = confirmPassword
        params["code"] = code
        params["login_access_token"] = loginAccessToken

        val request = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                try {
                    Log.d("::Response::", response)
                    val jsonResponse = JSONObject(response)
                    val errorCode = jsonResponse.getInt("error_code")
                    jsonResponse.getString("message")
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
            override fun getParams(): MutableMap<String, String> {
                Log.d("Params", params.toString())
                return params
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
}
