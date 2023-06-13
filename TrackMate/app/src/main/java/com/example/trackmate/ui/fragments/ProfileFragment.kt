package com.example.trackmate.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.trackmate.R
import com.example.trackmate.others.Constants
import com.example.trackmate.others.Constants.REQUEST_CODE_LOCATION_PERMISSIONS
import com.example.trackmate.others.TrackingUtility
import com.example.trackmate.repositories.MainRepository
import com.example.trackmate.ui.bottomsheets.BottomSheetsFragment
import com.example.trackmate.ui.viewmodels.MainViewModel
import com.example.trackmate.ui.viewmodels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cycle_statistic.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_run_statistics.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.round

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile), EasyPermissions.PermissionCallbacks {

    private val viewModel : MainViewModel by viewModels()
    private val viewModelStatistics : StatisticsViewModel by viewModels()

    @Inject
    lateinit var sharedPref : SharedPreferences

    @field:Named("Image")
    @JvmField
    @Inject
    var base64String = ""

    @field:Named("Name")
    @JvmField
    @Inject
    var userNickName = ""

    private lateinit var lottieAnimationView: LottieAnimationView

    private val swapRotate: Animation by lazy {
        AnimationUtils.loadAnimation(activity , R.anim.rotate_swap_btn);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()

        moreOptionsBtn.setOnClickListener {
            val mediaPlayer = MediaPlayer.create(
                context,
                R.raw.swip)
            mediaPlayer.start()
            moreOptionsBtn.startAnimation(swapRotate)
//            findNavController().navigate(
//                R.id.action_profileFragment_to_settingsFragment
//            )
            val bottomSheetsFragment = BottomSheetsFragment()
            bottomSheetsFragment.show(childFragmentManager , "BottomSheetDialog")
        }

//        if (base64String.isNotEmpty()) {
//            val byteArray = Base64.decode(base64String, Base64.DEFAULT)
//            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//            profile2.setImageBitmap(bitmap)
//        }

        val base64String = sharedPref.getString(Constants.KEY_IMG, "");
        val byteArray = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        profile2.setImageBitmap(bitmap)

        userName.text = sharedPref.getString(Constants.KEY_NAME, "");


        requestPermissions()

        arrow_waking.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_walkStatisticFragment
            )
        }

        arrow_running.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_runStatisticsFragment
            )
        }

        arrow_cycling.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_cycleStatisticFragment
            )
        }

        tvWalking.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_walkStatisticFragment
            )
        }
        tvRunning.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_runStatisticsFragment
            )
        }
        tvCycling.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_cycleStatisticFragment
            )
        }
    }


    private fun subscribeToObservers() {

        viewModelStatistics.totalCaloriesBurned.observe(viewLifecycleOwner , androidx.lifecycle.Observer {
            it?.let{
                val result = if (it < 1000) {
                    it.toString()
                } else {
                    val converted = it / 1000.0
                    if (converted >= 1) {
                        String.format("%.1fk", converted)
                    } else {
                        (it / 1000).toString() + "k"
                    }
                }
                tvTotalCaloriesBurned.text = result
            }
        })

        viewModelStatistics.totalDistance.observe(viewLifecycleOwner , androidx.lifecycle.Observer {
            it?.let{
                val km = it/1000
                val totalDistance = round(km * 10f) /10f
                val totalDistanceString = "${totalDistance}km"
                tvTotalDistanceCovered.text = totalDistanceString
            }
        })
        viewModelStatistics.totalAvgSpeed.observe(viewLifecycleOwner , androidx.lifecycle.Observer {
            it?.let{
                val avgSpeedInInt = it.toInt()
                tvAvgSpeed.text = "${avgSpeedInInt}Km/h"
            }
        })

        val calendar = Calendar.getInstance()
        val weekNumber = calendar.get(Calendar.WEEK_OF_YEAR)
        var totalCaloriesString = ""
        var totalCaloriesBurnedThisWeek1 = 0
        lifecycleScope.launch {
//            totalCaloriesBurnedThisWeek1 = viewModelStatistics.getTotalCaloriesBurnedThisWeek(weekNumber)
//            totalCaloriesString = "${totalCaloriesBurnedThisWeek1}Kcal"
//            Timber.d("Inside $totalCaloriesString")
            //tvProgress1.text = "${totalCaloriesBurnedThisWeek}Kcal"
            //progress_bar.progress = totalCaloriesBurnedThisWeek.toInt()

            val totalCaloriesBurnedThisWeek = viewModelStatistics.getTotalCaloriesBurnedThisWeek(weekNumber)
            val totalCaloriesString = "${totalCaloriesBurnedThisWeek}Kcal"
            withContext(Dispatchers.Main) {
                if (tvProgress1 != null) {
                    tvProgress1.text = totalCaloriesString
                }
                if(progress_bar!=null){
                    progress_bar.progress = totalCaloriesBurnedThisWeek.toInt()
                }
                //progress_bar.progress = totalCaloriesBurnedThisWeek.toInt()
            }
        }
//        Timber.d("solve  \"${totalCaloriesBurnedThisWeek1}Kcal\"")
//        tvProgress1.text = "${totalCaloriesBurnedThisWeek1}Kcal"
//        progress_bar.progress = totalCaloriesBurnedThisWeek1.toInt()

        var totalCaloriesBurnedLastWeek = 0
        var totalCaloriesBurnedThisWeek = 0
        var diff = 0
        lifecycleScope.launch {
            totalCaloriesBurnedLastWeek = viewModelStatistics.getTotalCaloriesBurnedThisWeek(weekNumber-1)
            totalCaloriesBurnedThisWeek = viewModelStatistics.getTotalCaloriesBurnedThisWeek(weekNumber)
            if(totalCaloriesBurnedLastWeek != null) {
                diff = totalCaloriesBurnedThisWeek - totalCaloriesBurnedLastWeek
                if(tvCaloriesBurnedStatus!=null){
                    tvCaloriesBurnedStatus.text = if(diff >= 0) "+${diff}" else "$diff"
                }
                //tvCaloriesBurnedStatus.text = if(diff >= 0) "+${diff}" else "$diff"
            }
        }
    }

    private fun requestPermissions(){
        Log.d("requestPermission 1" , " Inside this function")
        if(TrackingUtility.hasLocationPermissions(requireContext())){
            return
        }
        Log.d("requestPermission 2" , "Inside this function")
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app, Allow access all the time for better accuracy of app",
                REQUEST_CODE_LOCATION_PERMISSIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app, Allow access all the time for better accuracy of app",
                REQUEST_CODE_LOCATION_PERMISSIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    @SuppressLint("LongLogTag")
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this , perms)){
            Log.d("requestPermission 3" , " Inside this function")
            AppSettingsDialog.Builder(this).build().show()
        }else{
            Log.d("requestPermission 4" , " Inside this function")
            requestPermissions()
        }
    }

    @SuppressLint("LongLogTag")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("requestPermission 5" , " Inside this function")
        EasyPermissions.onRequestPermissionsResult(requestCode , permissions , grantResults,this)
    }
}