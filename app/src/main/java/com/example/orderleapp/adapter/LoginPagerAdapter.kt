package com.example.orderleapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.orderleapp.fragment.ForgotPasswordFragment
import com.example.orderleapp.fragment.LoginFragment

class LoginPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> ForgotPasswordFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}