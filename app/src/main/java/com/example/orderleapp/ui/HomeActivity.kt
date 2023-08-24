package com.example.orderleapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.orderleapp.R
import com.example.orderleapp.api.LogoutApi
import com.example.orderleapp.auth.LoginActivity
import com.example.orderleapp.databinding.ActivityHomeBinding
import com.example.orderleapp.fragment.AboutUsFragment
import com.example.orderleapp.fragment.ChangePasswordFragment
import com.example.orderleapp.fragment.DashboardFragment
import com.example.orderleapp.fragment.MyOrdersFragment
import com.example.orderleapp.fragment.NotificationFragment
import com.example.orderleapp.`interface`.CartCountObserver
import com.example.orderleapp.`object`.CartCountReceiverHolder
import com.example.orderleapp.`object`.Flags
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref


class HomeActivity : AppCompatActivity(),CartCountObserver {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CartCountReceiverHolder.register(this)
        Flags.init(this)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setDisplayShowCustomEnabled(true)
            setCustomView(R.layout.custom_action_bar)
        }
        if(savedInstanceState == null){
            replaceFragment(DashboardFragment(),0)
        }
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        CartCountReceiverHolder.unregister(this)
    }
    @SuppressLint("InflateParams")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)

        val cartItem = menu.findItem(R.id.action_cart)
        val cartIcon = cartItem.icon
        val sharedPreferences = getSharedPreferences("MyCartData", MODE_PRIVATE)

        // Inflate the custom badge layout
        val badgeLayout = layoutInflater.inflate(R.layout.menu_cart_action_layout, null)
        val badgeView = badgeLayout.findViewById<TextView>(R.id.badgeView)
        val badgeCount = sharedPreferences.all.size

        // Set badge count and show/hide badge
        if (badgeCount > 0) {
            badgeView.visibility = View.VISIBLE
            badgeView.text = badgeCount.toString()
        } else {
            badgeView.visibility = View.GONE
        }

        // Create an ImageView for the cart icon
        val cartIconImageView = ImageView(this)
        cartIconImageView.setImageDrawable(cartIcon)

        cartIconImageView.setOnClickListener {
            // Handle the click event here (e.g., open cart activity)
            startActivity(Intent(this, MyCartActivity::class.java))
        }

        // Add the badge layout and cart icon to the FrameLayout
        val badgeFrameLayout = FrameLayout(this)
        badgeFrameLayout.addView(cartIconImageView)
        badgeFrameLayout.addView(badgeLayout)

        // Set the FrameLayout as the action view for the cart menu item
        cartItem.actionView = badgeFrameLayout

        return super.onCreateOptionsMenu(menu)
    }
    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
    }

    private fun initView() {
        setDrawer()
        onClick()
    }
    private fun setDrawer() {
        val drawerLayout = binding.drawerLayout
        actionBarDrawerToggle = object :ActionBarDrawerToggle(this
            ,drawerLayout
            , R.string.nav_open
            , R.string.nav_close
        ){}
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    @SuppressLint("SetTextI18n")
    private fun onClick() {
        val drawerLayout = binding.drawerLayout

        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_dashboard -> {
                    replaceFragment(DashboardFragment(),0)
                    drawerLayout.closeDrawers()
                    binding.txtView.visibility = View.GONE

                }
                R.id.nav_myOrders -> {
                    replaceFragment(MyOrdersFragment(),0)
                    drawerLayout.closeDrawers()
                    binding.txtView.visibility = View.GONE

                }
                R.id.nav_terms -> {
                    replaceFragment(AboutUsFragment(),4)
                    drawerLayout.closeDrawers()
                    binding.txtView.visibility = View.VISIBLE
                    binding.txtView.text = "Terms & Condition"
                }
                R.id.nav_contacts -> {
                    replaceFragment(AboutUsFragment(),3)
                    drawerLayout.closeDrawers()
                    binding.txtView.visibility = View.VISIBLE
                    binding.txtView.text = "Contacts"
                }
                R.id.nav_aboutUs -> {
                    replaceFragment(AboutUsFragment(),1)
                    drawerLayout.closeDrawers()
                    binding.txtView.visibility = View.VISIBLE
                    binding.txtView.text = "About Us"
                }
                R.id.nav_bank -> {
                    replaceFragment(AboutUsFragment(),2)
                    drawerLayout.closeDrawers()
                    binding.txtView.visibility = View.VISIBLE
                    binding.txtView.text = "Bank Details"
                }
                R.id.nav_vision -> {
                    replaceFragment(AboutUsFragment(),5)
                    drawerLayout.closeDrawers()
                    binding.txtView.visibility = View.VISIBLE
                    binding.txtView.text = "Vision"
                }
                R.id.nav_notification -> {
                    replaceFragment(NotificationFragment(),0)
                    drawerLayout.closeDrawers()
                    binding.txtView.visibility = View.GONE
                }
                R.id.nav_change_pass -> {
                    replaceFragment(ChangePasswordFragment(),0)
                    drawerLayout.closeDrawers()
                    binding.txtView.visibility = View.GONE
                }
                R.id.nav_logout -> {
                    popUp()
                }
            }
            true
        }
    }
    private fun popUp() {
        val builder = AlertDialog.Builder(this,R.style.AlertDialogTheme)
        //set title for alert dialog
        builder.setTitle("Confirm")
        //set message for alert dialog
        builder.setMessage("Are you sure you wanna logout ??")
        builder.setIcon(R.drawable.ic_alert_box)

        //performing positive action
        builder.setPositiveButton("Yes"){ _, _ ->
            val userId = Pref.getValue(this, Config.PREF_USERID, "")
            val code = Pref.getValue(this, Config.PREF_CODE, "")
            val logoutApi = LogoutApi(this)

            logoutApi.logout(userId, code) { isSuccess ->
                if (isSuccess) {
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                    Toast.makeText(this, "Successfully Logged out !!", Toast.LENGTH_SHORT).show()
                    Flags.myFlag = false
                } else {
                    Toast.makeText(this, "Error!! \n Please try again later.", Toast.LENGTH_SHORT).show()
                    // The condition is not met or there was an error, handle accordingly
                }
            }
        }
        //performing negative action
        builder.setNegativeButton("No"){ _, _ -> }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        }else super.onOptionsItemSelected(item)
    }
    private fun replaceFragment(fragment: Fragment, pageNum: Int) {
       val  bundle = Bundle()
        if(pageNum != 0){
            bundle.putInt("page_id", pageNum)
            fragment.arguments = bundle
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    override fun onCartCountChanged() {
        invalidateOptionsMenu()
    }
}