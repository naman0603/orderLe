package com.example.orderleapp.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.orderleapp.R
import com.example.orderleapp.api.LoginApi
import com.example.orderleapp.databinding.FragmentLoginBinding
import com.example.orderleapp.`object`.Flags
import com.example.orderleapp.ui.HomeActivity
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging


@Suppress("SENSELESS_COMPARISON")
class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    private lateinit var txtUsername : EditText
    private lateinit var txtPassword : EditText
    private lateinit var sp : SharedPreferences
    private var oldPass:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        Flags.init(requireContext())

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        tokenRefresh()
        onClick()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun tokenRefresh() {
        val refreshedToken: String = Firebase.messaging.token.toString()
        //Displaying token on logcat
        if (refreshedToken != null) {
            Pref.setValue(requireContext(), Config.PREF_DEVICE_TOKEN, refreshedToken)
        }
    }

    private fun onClick() {
        val viewPager: ViewPager2? = activity?.findViewById(R.id.viewPager)

        binding.txtForgot.setOnClickListener {
            viewPager?.setCurrentItem(1, true)
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPassword.text.toString()

            login(username,password)
        }
    }

    private fun initView() {
        txtUsername = binding.txtUsername
        txtPassword = binding.txtPassword
    }

    private fun login(email: String, password: String) {
        val loginApi = LoginApi { _ ->
            // Handle successful login response
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            activity?.finish()
            Flags.myFlag = true
            Toast.makeText(requireContext(), "Successfully Logged In !!", Toast.LENGTH_SHORT).show()

            // Handle any additional data processing if needed
        }

        loginApi.login(
            requireContext(),
            email,
            password,
            Pref.getValue(requireContext(),Config.PREF_DEVICE_UNIQUE_ID,"").toString(),
            Pref.getValue(requireContext(),Config.PREF_DEVICE_TOKEN,"").toString(),
            "1"
        )
    }
}