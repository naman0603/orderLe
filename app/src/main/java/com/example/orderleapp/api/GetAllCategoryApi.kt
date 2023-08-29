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
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.orderleapp.R
import com.example.orderleapp.apiResponse.CategoryApiResponse
import com.example.orderleapp.auth.LoginActivity
import com.example.orderleapp.`object`.Flags
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref
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
                    val errorCode = jsonResponse.getInt("error_code")
                    if(errorCode == 0 ){
                        val apiResponse = parseApiResponse(dataArray)
                        onSuccess(apiResponse)
                    }else if(errorCode == 4){
                        popUp()
                    }
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
                context.finish() // Finish the activity if the context is an instance of Activity
            }
            Flags.myFlag = false
            builder.setCancelable(false)
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
