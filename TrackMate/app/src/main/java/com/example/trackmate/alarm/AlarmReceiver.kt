package com.example.trackmate.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.trackmate.R
import com.example.trackmate.others.Constants
import com.example.trackmate.services.TrackingService

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: return
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(context, "alarm_channel")
            .setSmallIcon(R.drawable.location1)
            .setContentTitle("It's Been 24 hrs")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notificationManager.notify(1, notificationBuilder.build())
    }
}