package com.example.orderleapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.orderleapp.adapter.LoginPagerAdapter
import com.example.orderleapp.databinding.ActivityLoginBinding
import com.example.orderleapp.`object`.Flags
import com.example.orderleapp.ui.HomeActivity

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Flags.init(this)
        onCLick()
    }

    private fun onCLick() {
        val viewPager: ViewPager2 = binding.viewPager
        val pagerAdapter = LoginPagerAdapter(this)
        viewPager.adapter = pagerAdapter
    }

    override fun onStart() {
        if(Flags.myFlag){
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
        super.onStart()
    }

}