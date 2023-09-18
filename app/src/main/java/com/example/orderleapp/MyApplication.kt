package com.example.orderleapp

import android.app.Application
import com.example.orderleapp.util.DatabaseHelper

class MyApplication : Application() {
    companion object {
        lateinit var databaseHelper: DatabaseHelper
    }

    override fun onCreate() {
        super.onCreate()
        databaseHelper = DatabaseHelper(this)
    }
}
