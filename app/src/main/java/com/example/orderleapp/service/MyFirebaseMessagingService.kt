package com.example.orderleapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.orderleapp.R
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle the incoming message and display the notification
        // You can customize the notification content here

        // Extract data from the message payload
        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]

        // Show the notification
        showNotification(title, body)
    }




    private fun showNotification(title: String?, body: String?) {
        val notificationManager = getSystemService(NotificationManager::class.java)

        // Check if the target SDK is 26 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setAutoCancel(true)
            .build()

        // Notification ID allows you to update or cancel the notification later
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
    }
}







