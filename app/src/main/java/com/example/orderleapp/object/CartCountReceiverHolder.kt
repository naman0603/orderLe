package com.example.orderleapp.`object`

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.example.orderleapp.`interface`.CartCountObserver

object CartCountReceiverHolder {

    // Action for the broadcast
    const val ACTION_CART_COUNT_CHANGED = "com.example.orderleapp.ACTION_CART_COUNT_CHANGED"

    // Broadcast receiver to handle the cart count change
    private val cartCountReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_CART_COUNT_CHANGED) {
                // Handle cart count change here (e.g., update badge)
                if (context != null) {
                    (context as? CartCountObserver)?.onCartCountChanged()
                }
            }
        }
    }

    // Register the broadcast receiver
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun register(context: Context) {
        val intentFilter = IntentFilter(ACTION_CART_COUNT_CHANGED)
        context.registerReceiver(cartCountReceiver, intentFilter)
    }

    // Unregister the broadcast receiver
    fun unregister(context: Context) {
        context.unregisterReceiver(cartCountReceiver)
    }

    // Send broadcast to notify the activity about the cart count change
    fun sendCartCountChangedBroadcast(context: Context) {
        val intent = Intent(ACTION_CART_COUNT_CHANGED)
        context.sendBroadcast(intent)
    }
}
