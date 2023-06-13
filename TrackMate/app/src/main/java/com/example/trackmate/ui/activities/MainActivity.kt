package com.example.trackmate.ui.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.trackmate.R
import com.example.trackmate.alarm.AlarmItem
import com.example.trackmate.alarm.AndroidAlarmScheduler
import com.example.trackmate.db.TrackDao
import com.example.trackmate.others.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.example.trackmate.ui.viewmodels.MainViewModel
import com.example.trackmate.worker.PostsCheckWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModels()

    @Inject
    lateinit var sharedPref : SharedPreferences
    @Inject
    lateinit var trackDao: TrackDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scheduleDataWorker()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alarm Channel"
            val descriptionText = "Channel for alarm notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("alarm_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        Timber.d("inside MainActivity ON create")

        navigateToTrackingFragmentIfNeeded(intent)

        Timber.d("inside MainActivity ON create 2")

        Log.d("TrackDao", "TrackDao :  + ${trackDao.hashCode()}")
        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        navHostFragment.findNavController().addOnDestinationChangedListener{_,destination,_ ->
            when(destination.id){
                R.id.profileFragment , R.id.tracksFragment , R.id.startFragment , R.id.statisticsFragment , R.id.feedFragment ->
                    bottomNavigationView.visibility = View.VISIBLE
                else -> bottomNavigationView.visibility = View.GONE
            }
        }

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.statusBarColor = ContextCompat.getColor(this , R.color.topBar1)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.d("inside onNewIntent")
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent : Intent?){
        Timber.d("inside navigateToStartFragmentIfNeeded 1")
        if(intent?.action == ACTION_SHOW_TRACKING_FRAGMENT){
            Timber.d("inside navigateToStartFragmentIfNeeded 2")
            navHostFragment.findNavController().navigate(R.id.action_global_trackingFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        val alarmScheduler = AndroidAlarmScheduler(this)
        alarmScheduler.schedule(AlarmItem(LocalDateTime.now(), "Notification message"))
    }

    private fun scheduleDataWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val dataWorkRequest = PeriodicWorkRequestBuilder<PostsCheckWorker>(
            1 , TimeUnit.MINUTES // Run the Worker every 1 hour
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "PostsCheckWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            dataWorkRequest
        )
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        Timber.d("inside onNewIntent")
//        navigateToStartFragmentIfNeeded(intent)
//    }
//
//    private fun navigateToStartFragmentIfNeeded(intent : Intent?) {
//        Timber.d("inside navigateToStartFragmentIfNeeded 1")
//        if(intent?.action == ACTION_SHOW_TRACKING_FRAGMENT){
//            Timber.d("inside navigateToStartFragmentIfNeeded 2")
//            navHostFragment.findNavController().navigate(R.id.startFragment)
//        }
//    }
}