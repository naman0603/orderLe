package com.example.orderleapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.orderleapp.R
import com.example.orderleapp.api.ForgotPasswordApi
import com.example.orderleapp.auth.LoginActivity
import com.example.orderleapp.databinding.FragmentForgotPasswordBinding


class ForgotPasswordFragment : Fragment() {
    lateinit var binding: FragmentForgotPasswordBinding
    lateinit var txtEmail : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }
    private fun initView() {
        txtEmail = binding.txtUsername
        onClick()
    }

    private fun onClick() {
        val viewPager: ViewPager2? = activity?.findViewById(R.id.viewPager)
        binding.txtLogin.setOnClickListener {
            viewPager?.setCurrentItem(0, true)
        }
        binding.btnSubmit.setOnClickListener {
            binding.pgBar.visibility = View.VISIBLE
            forgotPasswordApi()
        }
    }

    private fun forgotPasswordApi() {


            val forgotPasswordApi = ForgotPasswordApi(requireContext()) { isSuccess ->
                if (isSuccess) {
                    binding.pgBar.visibility = View.INVISIBLE
                    txtEmail.text.clear()
                    Toast.makeText(requireContext(), "Mail Has Been Sent ", Toast.LENGTH_SHORT).show()
                } else {
                    binding.pgBar.visibility = View.INVISIBLE

                    Toast.makeText(requireContext(), "Error Please Try Again", Toast.LENGTH_SHORT).show()
                }
            }

        val email = txtEmail.text.trim().toString()
        forgotPasswordApi.forgotPassword(email)

    }
}