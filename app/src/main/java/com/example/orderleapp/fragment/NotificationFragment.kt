package com.example.orderleapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.orderleapp.databinding.FragmentNotificationBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog


class NotificationFragment : Fragment(),EasyPermissions.PermissionCallbacks {

    private lateinit var binding: FragmentNotificationBinding
    companion object{
        const val PERMISSION_REQUEST_CODE = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding  = FragmentNotificationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        val switch = binding.switchBtn
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if(hasPermissions()){
                    Toast.makeText(requireContext(), "Notification Enabled", Toast.LENGTH_SHORT).show()
                }else{
                    requestPermissions()
                }
            } else {
                Toast.makeText(requireContext(), "Notification Disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            SettingsDialog.Builder(requireContext()).build().show()
        }else{
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
    }

    private fun hasPermissions() =
        EasyPermissions.hasPermissions(
            requireContext(),
            android.Manifest.permission.POST_NOTIFICATIONS
        )

    private fun requestPermissions(){
        EasyPermissions.requestPermissions(
            this,
            "This Permissions are Necessary",
            PERMISSION_REQUEST_CODE,
            android.Manifest.permission.POST_NOTIFICATIONS

        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }
}