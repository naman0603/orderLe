package com.example.orderleapp.`object`

import android.content.Context
import android.content.SharedPreferences

object Flags {
    private const val PREFS_NAME = "MyFlags"
    private const val MY_FLAG_KEY = "myFlag"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var myFlag: Boolean
        get() = sharedPreferences.getBoolean(MY_FLAG_KEY, false)
        set(value) = sharedPreferences.edit().putBoolean(MY_FLAG_KEY, value).apply()
}
