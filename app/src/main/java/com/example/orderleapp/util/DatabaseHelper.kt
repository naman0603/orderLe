package com.example.orderleapp.util

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.orderleapp.apiResponse.ProductApiResponse
import com.example.orderleapp.dataModel.ProductDetailBean
import com.example.orderleapp.dataModel.ProductTypeBean
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "orderle_database.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE favourite (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT," +
                "product_data TEXT" +
                ");"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS favourite")
        onCreate(db)
    }

    fun insertProductApiResponse(id: Int, name: String, productApiResponse: ProductApiResponse): Long {
        val db = writableDatabase

        // Check if the data with the same name already exists
        val cursor = db.query("favourite", arrayOf("name"), "name=?", arrayOf(name), null, null, null)
        if (cursor != null && cursor.count > 0) {
            // Data with the same name already exists, return 1
            cursor.close()
            return 1
        }
        cursor?.close()

        // Data doesn't exist, proceed with insertion
        val values = ContentValues()
        values.put("id",id)
        values.put("name", name)
        values.put("product_data", Gson().toJson(productApiResponse))
        return db.insert("favourite", null, values)
    }
    fun getAllProductApiResponses(): List<ProductApiResponse> {
        val db = readableDatabase
        val projection = arrayOf("id", "name", "product_data")
        val cursor = db.query("favourite", projection, null, null, null, null, null)

        val productApiResponses = mutableListOf<ProductApiResponse>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val productData = cursor.getString(cursor.getColumnIndexOrThrow("product_data"))
            val data = JSONObject(productData)
            Log.d("TAG_DATA",data.toString())
            val response = parseApiResponse(data)
            //val productApiResponse = Gson().fromJson(productData, ProductApiResponse::class.java)
            productApiResponses.add(response!!)
        }
        cursor.close()
        return productApiResponses
    }
    fun removeProductApiResponse(id: Int): Int {
        val db = writableDatabase
        return db.delete("favourite", "id=?", arrayOf(id.toString()))
    }
    private fun parseApiResponse(jsonObject: JSONObject): ProductApiResponse? {
        val productDetailBeansArray = jsonObject.optJSONArray("productDetailBeans")
        val productTypeBeansArray = jsonObject.optJSONArray("productTypeBeans")
        val ringSizeArray = jsonObject.optJSONArray("ringSizeArray")

        val productDetailBeans = parseProductDetailBeans(productDetailBeansArray)
        val productTypeBeans = parseProductTypeBeans(productTypeBeansArray)
        val ringSizeList = parseRingSizeArray(ringSizeArray)

        val productApiResponse = ProductApiResponse(
            id = jsonObject.optInt("id", 0),
            productId = jsonObject.optInt("product_id", 0),
            productCategoryId = jsonObject.optInt("product_category_id", 0),
            productName = jsonObject.optString("product_name", ""),
            productDescription = jsonObject.optString("product_description", ""),
            productPictureUrl = jsonObject.optString("product_picture_url", ""),
            productPicture = jsonObject.optString("product_picture", ""),
            productWeight = jsonObject.optDouble("product_weight", 0.0),
            createdDate = jsonObject.optString("created_date", ""),
            wastage = jsonObject.optString("wastage", ""),
            stoneCharge = jsonObject.optString("stone_charge", ""),
            totalStoneCharge = jsonObject.optInt("total_stone_charge", 0),
            categoryTitle = jsonObject.optString("category_title", ""),
            itemCode = jsonObject.optString("item_code", ""),
            requestMasterId = jsonObject.optInt("request_master_id", 0),
            userId = jsonObject.optInt("user_id", 0),
            requestNumber = jsonObject.optString("request_number", ""),
            productRequestId = jsonObject.optInt("product_request_id", 0),
            quantity = jsonObject.optInt("quantity", 0),
            partyName = jsonObject.optString("party_name", ""),
            partyEmail = jsonObject.optString("party_email", ""),
            partyPhone = jsonObject.optString("party_phone", ""),
            orderStatus = jsonObject.optInt("order_status", 0),
            invoiceFile = jsonObject.optString("invoice_file", ""),
            qty = jsonObject.optInt("qty", 1),
            productTotalWeight = jsonObject.optDouble("product_total_weight", 0.0),
            categoryName = jsonObject.optString("category_name", ""),
            ringSize = jsonObject.optString("ringSize", ""),
            isChecked = jsonObject.optBoolean("isChecked", false),
            ringSizeArray = ringSizeList.toTypedArray(),
            orderDescription = jsonObject.optString("order_description", ""),
            goldType = jsonObject.optString("gold_type", ""),
            goldTypeId = jsonObject.optInt("goldtype_id", 0),
            goldCarat = jsonObject.optString("gold_carat", ""),
            productTypeBeans = productTypeBeans,
            productDetailBeans = productDetailBeans
        )

        // Print the parsed ProductApiResponse object
        return productApiResponse
    }

    fun parseProductDetailBeans(jsonArray: JSONArray?): ArrayList<ProductDetailBean> {
        val result = mutableListOf<ProductDetailBean>()
        jsonArray?.let {
            for (i in 0 until it.length()) {
                val product = it.optJSONObject(i)
                product?.let {
                    val productId = it.optString("product_id", "")
                    result.add(ProductDetailBean(productId))
                }
            }
        }
        return result as ArrayList<ProductDetailBean>
    }

    fun parseProductTypeBeans(jsonArray: JSONArray?): ArrayList<ProductTypeBean> {
        val result = mutableListOf<ProductTypeBean>()
        jsonArray?.let {
            for (i in 0 until it.length()) {
                val productType = it.optJSONObject(i)
                productType?.let {
                    val mediaType = it.optInt("media_type", 0)
                    val productImage = it.optString("product_image", "")
                    result.add(ProductTypeBean(productImage, mediaType,""))
                }
            }
        }
        return result as ArrayList<ProductTypeBean>
    }

    fun parseRingSizeArray(jsonArray: JSONArray?): List<String> {
        val result = mutableListOf<String>()
        jsonArray?.let {
            for (i in 0 until it.length()) {
                val ringSize = it.optString(i, "")
                result.add(ringSize)
            }
        }
        return result
    }
}