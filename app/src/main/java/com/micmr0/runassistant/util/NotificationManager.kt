package com.micmr0.runassistant.util

import android.app.NotificationChannel
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat

internal class NotificationManager(val context: Context) {

    companion object {
        private val CHANNEL_ID = "YOUR_CHANNEL_ID"
        private val CHANNEL_NAME = "Your human readable notification channel mName"
        private val CHANNEL_DESCRIPTION = "description"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMainNotificationId(): String {
        return CHANNEL_ID
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createMainNotificationChannel() {
        val id = CHANNEL_ID
        val name = CHANNEL_NAME
        val description = CHANNEL_DESCRIPTION
        val importance = android.app.NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel(id, name, importance)
        mChannel.description = description
        mChannel.enableLights(true)
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        mNotificationManager.createNotificationChannel(mChannel)
    }

    fun createNotificationCompatBuilder(context: Context): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return NotificationCompat.Builder(context, NotificationManager(context).getMainNotificationId())
        } else {
            return NotificationCompat.Builder(context)
        }
    }
}
