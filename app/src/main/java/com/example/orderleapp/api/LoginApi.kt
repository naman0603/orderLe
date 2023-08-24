package com.example.orderleapp.api


import android.content.Context
import android.util.Log
import com.example.orderleapp.apiResponse.LoginApiResponse
import com.example.orderleapp.`interface`.ApiService
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("SENSELESS_COMPARISON")
class LoginApi(private val onSuccess: (LoginApiResponse) -> Unit) {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Config.LOGIN_API + "/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    // Define API methods here
    fun login(
        context: Context,
        email: String,
        password: String,
        deviceUniqueId: String,
        deviceToken: String,
        userType: String
    ) {
        val call = apiService.login(email, password, deviceUniqueId, deviceToken, userType)

        call.enqueue(object : Callback<LoginApiResponse> {
            override fun onResponse(call: Call<LoginApiResponse>, response: Response<LoginApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        if (apiResponse.errorCode == 0 && apiResponse.success) {
                            parse(context, apiResponse)
                            onSuccess(apiResponse) // Call the success callback
                        } else {
                            // Handle error
                        }
                    }
                } else {
                    Log.e("Login Error", "API request error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginApiResponse>, t: Throwable) {
                Log.e("Login Error", "API request failed: ${t.message}")
            }
        })
    }

    /*
    Using Volley
fun login(
        context: Context,
        email: String,
        password: String,
        deviceUniqueId: String,
        deviceToken: String,
        userType: String
    ) {
        val url = Config.API_LOGIN_USER

        val params = HashMap<String, String>()
        params["username"] = email
        params["password"] = password
        params["device_unique_id"] = deviceUniqueId
        params["device_token"] = deviceToken
        params["device_type"] = userType

        val request = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    Log.d("::Response::", "$response")
                    val jsonResponse = JSONObject(response)
                    val apiResponse = ApiResponse(
                        errorCode = jsonResponse.getInt("error_code"),
                        message = jsonResponse.getString("message"),
                        success = jsonResponse.getBoolean("success"),
                        // Parse other data fields as needed
                    )

                    parse(context, apiResponse)
                    onSuccess(apiResponse) // Call the success callback
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Log.e(":: Login Error ::", "API request error: ${error.message}")
            }
        ) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }
        Volley.newRequestQueue(context).add(request)
    }
*/

    private fun parse(context: Context, response: LoginApiResponse) {
        if (response != null) {
            Log.d("::: API_LOGIN_USER RESPONSE:::", response.toString())
            val code = response.errorCode
            val status = response.success

            if (code == 0 && status) {
                val apiData = response.data

                if (apiData != null) {
                    val userDataList = apiData.userData
                    val brandingDataList = apiData.brandingData

                    if (userDataList.isNotEmpty()) {
                        val userData = userDataList[0]

                        // Check for properties and set values
                        if (userData.companyId != null) {
                            Log.d("PREF_COMPANY_ID",Config.PREF_COMPANY_ID)
                            Pref.setValue(context, Config.PREF_COMPANY_ID, userData.companyId.toInt())
                        }
                        if (userData.code != null) {
                            Pref.setValue(context, Config.PREF_CODE, userData.code)
                        }
                        if (userData.userId != null) {
                            Pref.setValue(context, Config.PREF_USERID, userData.userId)
                        }
                        if (userData.name != null) {
                            Pref.setValue(context, Config.PREF_NAME, userData.name)
                        }
                        if (userData.email != null) {
                            Pref.setValue(context, Config.PREF_EMAIL, userData.email)
                        }
                        if (userData.username != null) {
                            Pref.setValue(context, Config.PREF_USERNAME, userData.username)
                        }
                        if (userData.deviceUniqueId != null) {
                            Pref.setValue(context, Config.PREF_DEVICE_UNIQUE_ID, userData.deviceUniqueId)
                        }
                        if (userData.deviceToken != null) {
                            Pref.setValue(context, Config.PREF_DEVICE_TOKEN, userData.deviceToken)
                        }
                        if (userData.loginAccessToken != null) {
                            Pref.setValue(context, Config.PREF_LOGIN_ACCESS_TOKEN, userData.loginAccessToken)
                        }
                    }

                    if (brandingDataList.isNotEmpty()) {
                        val brandingData = brandingDataList[0]

                        // Check for properties and set values
                        if (brandingData.id != null) {
                            Pref.setValue(context, Config.PREF_BRANDING_ID, brandingData.id)
                        }
                        if (brandingData.companyId != null) {
                            Pref.setValue(context, Config.PREF_BRANDING_COMPANY_ID, brandingData.companyId)
                        }
                        if (brandingData.code != null) {
                            Pref.setValue(context, Config.PREF_BRANDING_CODE, brandingData.code)
                        }
                        if (brandingData.name != null) {
                            Pref.setValue(context, Config.PREF_BRANDING_NAME, brandingData.name)
                        }
                        if (brandingData.logo != null) {
                            Pref.setValue(context, Config.PREF_LOGO, brandingData.logo)
                        }
                        if (brandingData.brandingLogo != null) {
                            Pref.setValue(context, Config.PREF_BRANDING_LOGO, brandingData.brandingLogo)
                        }
                        if (brandingData.contact != null) {
                            Pref.setValue(context, Config.PREF_BRANDING_CONTACT, brandingData.contact)
                        }
                        if (brandingData.address != null) {
                            Pref.setValue(context, Config.PREF_BRANDING_ADDRESS, brandingData.address)
                        }
                        if (brandingData.gst != null) {
                            Pref.setValue(context, Config.PREF_BRANDING_GST, brandingData.gst)
                        }
                        if (brandingData.cin != null) {
                            Pref.setValue(context, Config.PREF_BRANDING_CIN, brandingData.cin)
                        }
                        if (brandingData.pan != null) {
                            Pref.setValue(context, Config.PREF_BRANDING_PAN, brandingData.pan)
                        }
                        if (brandingData.fromEmailAddress != null) {
                            Pref.setValue(context, Config.PREF_BRANDING_FROM_EMAIL, brandingData.fromEmailAddress)
                        }
                        if (brandingData.logoUrl != null) {
                            Pref.setValue(context, Config.PREF_BRANDING_LOGO_URL, brandingData.logoUrl)
                        }
                    }
                }
            }
        }
    }
}
