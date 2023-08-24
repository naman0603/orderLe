package com.example.orderleapp.api

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.orderleapp.apiResponse.BannerListApiResponse
import com.example.orderleapp.util.Config
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class BannerListApi(private val context: Context, private val onSuccess: (List<BannerListApiResponse>) -> Unit) {

    fun getBannerList(userId: String, code: String, loginAccessToken: String) {
        val url = Config.API_GET_BANNERS

        val params = HashMap<String, String>()
        params["user_id"] = userId
        params["code"] = code
        params["login_access_token"] = loginAccessToken
        Log.w("Params",params.toString())

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
                Log.e("Banner List Error", "API request error: ${error.message}")
            }
        ) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }

        Volley.newRequestQueue(context).add(request)
    }

    private fun parseApiResponse(jsonArray: JSONArray): List<BannerListApiResponse> {
        val bannerList: MutableList<BannerListApiResponse> = mutableListOf()

        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.getJSONObject(i)
            val bannerId = jsonObj.getString("banner_id")
            val banner = jsonObj.getString("banner")
            val createdDate = jsonObj.getString("created_date")

            val bannerApiResponse = BannerListApiResponse(
                bannerId, banner, createdDate
            )

            bannerList.add(bannerApiResponse)
        }
        Log.d("bannerList",bannerList.toString())
        return bannerList
    }
}
