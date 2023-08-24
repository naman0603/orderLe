package com.example.orderleapp.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.orderleapp.api.ChangePasswordApi
import com.example.orderleapp.databinding.FragmentChangePasswordBinding
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref


class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var sp : SharedPreferences
    private var oldPass:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference()
        initView()
    }

    private fun sharedPreference() {
        sp = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        oldPass = sp.getString("pass", null).toString()
    }

    private fun initView() {
        binding.btnChangePass.setOnClickListener {
            binding.pgBar.visibility = View.VISIBLE
            val oldPassword = binding.oldPassword.text.toString()
            val newPass = binding.newPassword.text.toString()
            val conNewPass = binding.confirmPassword.text.toString()

            if(oldPassword!=oldPass){
                binding.pgBar.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), "Incorrect Old Password", Toast.LENGTH_SHORT).show()
            }else if(newPass!=conNewPass){
                binding.pgBar.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), "Password Doesnt Match", Toast.LENGTH_SHORT).show()
            }else{
                changePasswordApi(oldPassword,newPass,conNewPass)
            }
        }

    }

    private fun changePasswordApi(oldPass: String, newPass: String, conNewPass: String) {
        val changePasswordApi = ChangePasswordApi(requireContext()) { isSuccess ->
            if (isSuccess) {
               binding.oldPassword.text!!.clear()
                binding.newPassword.text!!.clear()
                binding.confirmPassword.text!!.clear()
                Toast.makeText(requireContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show()
                binding.pgBar.visibility = View.INVISIBLE

            } else {
                binding.pgBar.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), "Please Try Again Later", Toast.LENGTH_SHORT).show()
              }
        }

        val userId = Pref.getValue(requireContext(), Config.PREF_USERID, "").toString()
        val code = Pref.getValue(requireContext(), Config.PREF_CODE, "")
        val loginAccessToken = Pref.getValue(requireContext(), Config.PREF_LOGIN_ACCESS_TOKEN, "")

        changePasswordApi.changePassword(
            userId,
            oldPass,
            newPass,
            conNewPass,
            code,
            loginAccessToken
        )

    }
}