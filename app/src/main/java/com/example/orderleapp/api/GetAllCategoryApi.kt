package com.example.orderleapp.api


import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.orderleapp.apiResponse.CategoryApiResponse
import com.example.orderleapp.util.Config
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class GetAllCategoryApi(
    private val context: Context,
    private val onSuccess: (List<CategoryApiResponse>) -> Unit
) {

    fun getCategoryList(
        userId: String,
        code: String,
        caratId: String,
        loginAccessToken: String
    ) {
        val url = Config.API_GET_ALL_CATEGORY

        val params = HashMap<String, String>()
        params["user_id"] = userId
        params["code"] = code
        if(caratId=="0"){
            params["carat_id"] = ""
        }else{
            params["carat_id"] = caratId
        }
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
                Log.e("Category List Error", "API request error: ${error.message}")
            }
        ) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }

        Volley.newRequestQueue(context).add(request)
    }

    private fun parseApiResponse(jsonArray: JSONArray): List<CategoryApiResponse> {
        val categoryList: MutableList<CategoryApiResponse> = mutableListOf()

        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.getJSONObject(i)
            val categoryId = jsonObj.getInt("category_id")
            val categoryTitle = jsonObj.getString("category_title")
            val categoryPictureUrl = jsonObj.getString("category_picture_url")
            val categoryPicture = jsonObj.getString("category_picture")

            val categoryApiResponse = CategoryApiResponse(
                0, 0, "", "", categoryId, categoryTitle, categoryPictureUrl, categoryPicture
            )

            categoryList.add(categoryApiResponse)
        }
        Log.d("categoryList", categoryList.toString())
        return categoryList
    }
}
