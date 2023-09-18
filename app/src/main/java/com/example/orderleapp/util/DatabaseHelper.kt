package com.example.orderleapp.util

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.orderleapp.apiResponse.ProductApiResponse
import com.google.gson.Gson

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
            val productApiResponse = Gson().fromJson(productData, ProductApiResponse::class.java)
            productApiResponses.add(productApiResponse)
        }
        cursor.close()
        return productApiResponses
    }
    fun removeProductApiResponse(id: Int): Int {
        val db = writableDatabase
        return db.delete("favourite", "id=?", arrayOf(id.toString()))
    }
}