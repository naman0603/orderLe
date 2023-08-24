package com.example.orderleapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Secure
import androidx.appcompat.app.AppCompatActivity
import com.example.orderleapp.auth.LoginActivity
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()

    }

    @SuppressLint("HardwareIds")
    private fun initView() {
        //User of get DEVICE UNIQUE ID
        val android_id = Secure.getString(contentResolver, Secure.ANDROID_ID)
        Pref.setValue(this@SplashActivity, Config.PREF_DEVICE_UNIQUE_ID, android_id)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        },2000)
    }
}