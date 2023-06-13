package com.example.trackmate.worker

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.trackmate.R
import com.example.trackmate.db.api.posts.services.DisplayPostService
import com.example.trackmate.db.api.ServiceBuilder
import com.example.trackmate.others.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PostsCheckWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {


    override fun doWork(): Result {
        try{

            val sharedPreferences = applicationContext.getSharedPreferences(Constants.SHARED_PREFRENCES_NAME, Context.MODE_PRIVATE)
            val previousPostCount = sharedPreferences.getInt(Constants.KEY_POST_COUNT, 0)

            Log.d("Worker Testing" , "Worker Called")
        val displayPostService: DisplayPostService = ServiceBuilder.buildService(DisplayPostService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = displayPostService.getPosts("https://track-mate-backend-app.onrender.com/api/post/getPost", 1)

            val newPostCount = response.totalCount

            if (newPostCount > previousPostCount) {
                sendNotification()
            }

            val editor = sharedPreferences.edit()
            editor.putInt(Constants.KEY_POST_COUNT, newPostCount)
            editor.apply()
        }
        return Result.success()
    } catch (e: Exception) {
        return Result.failure()
    }
    }

    @SuppressLint("ServiceCast")
    private fun sendNotification() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
        val notification = NotificationCompat.Builder(applicationContext, "channel_id")
            .setContentTitle("New Posts")
            .setContentText("New Posts created by your friend")
            .setSmallIcon(R.drawable.location1)
            .setAutoCancel(true)
            .build()

        // Send the notification
        notificationManager.notify(1, notification)
    }

    @SuppressLint("ServiceCast")
    private fun isAppInForeground(): Boolean {
        val activityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses

        for (appProcess in appProcesses) {
            if (appProcess.processName == applicationContext.packageName && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }

        return false
    }
}