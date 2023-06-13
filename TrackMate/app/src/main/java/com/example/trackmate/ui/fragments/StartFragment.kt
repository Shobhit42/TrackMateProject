package com.example.trackmate.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieAnimationView
import com.example.trackmate.R
import com.example.trackmate.db.Track
import com.example.trackmate.others.Constants
import com.example.trackmate.others.Constants.ACTION_PAUSE_SERVICE
import com.example.trackmate.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.trackmate.others.Constants.ACTION_STOP_SERVICE
import com.example.trackmate.others.Constants.MAP_ZOOM
import com.example.trackmate.others.Constants.POLYLINE_COLOR
import com.example.trackmate.others.Constants.POLYLINE_WIDTH
import com.example.trackmate.others.TrackingUtility
import com.example.trackmate.others.TrackingUtility.kmhToMpm
import com.example.trackmate.services.Polyline
import com.example.trackmate.services.Polylines
import com.example.trackmate.services.TrackingService
import com.example.trackmate.ui.viewmodels.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_setup.*
import kotlinx.android.synthetic.main.fragment_start.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.round


@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var sharedPref : SharedPreferences

    @field:Named("Mode")
    @JvmField
    @Inject
    var userSelectedMode = "Walking"

    @field:Named("Gender")
    @JvmField
    @Inject
    var userGender = "Male"

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var showDialog = true

    private var map: GoogleMap? = null

    private var curTimeInMillis = 0L

    lateinit var dialog : Dialog
    lateinit var dialogToggle : Dialog

    @set:Inject
    var weight = 60

    private lateinit var lottieAnimationView: LottieAnimationView

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(activity , R.anim.rotate_open_anim);
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(activity , R.anim.rotate_close_anim);
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(activity , R.anim.from_bottom_anim);
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(activity , R.anim.to_bottom_anim);
    }

    private var clicked = false;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        dialog = activity?.let { Dialog(it) }!!
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT)

        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog)

        dialogToggle = activity?.let { Dialog(it) }!!
        dialogToggle.setContentView(R.layout.custom_dialog_toggle)
        dialogToggle.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT)

        dialogToggle.window?.setBackgroundDrawableResource(R.drawable.bg_dialog)

        btnToggleStart.setOnClickListener {
            if (btnToggleStart.text == "Start Tracking" && btnFinishStart.visibility == View.GONE) {
                dialogToggle.show()
            }
            toggleRun()
        }

        btnFinishStart.setOnClickListener {
            zoomToSeeWholeTrack()
            endRunAndSaveToDb()
        }

        mapView.getMapAsync {
            map = it
            addAllPolylines()
            //map?.mapType = GoogleMap.MAP_TYPE_NORMAL
        }

        moreBtn.setOnClickListener{
            onMoreButtonClicked();
        }
//        val bottomSheetsFragment = BottomSheetsFragment()
//        changeThemeBtn.setOnClickListener {
//            bottomSheetsFragment.show(childFragmentManager, "BottomSheetDialog")
//        }
        cancelBtn.setOnClickListener {
            Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show();
        }

        var No : Button
        var Yes : Button
        No = dialog.findViewById(R.id.btnReset1)
        Yes = dialog.findViewById(R.id.btnDone1)

        Yes.setOnClickListener {
            dialog.dismiss()
            stopTrack();
        }

        No.setOnClickListener {
            dialog.dismiss()
        }

        lottieAnimationView = dialogToggle.findViewById(R.id.lottie1);
        lottieAnimationView.playAnimation();

        var toggleButtonGroup : MaterialButtonToggleGroup
        toggleButtonGroup = dialogToggle.findViewById(R.id.toggleButtonGroup)
        toggleButtonGroup.addOnButtonCheckedListener { toggleButtonGroup, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.walkToggleBtn -> {
                        showToast("Start Walking")
                        userSelectedMode = "Walking"
                        sharedPref.edit()
                            .putString(Constants.KEY_MODE, userSelectedMode)
                            .apply()
                    }
                    R.id.runToggleBtn -> {
                        showToast("Start Running")
                        userSelectedMode = "Running"
                        sharedPref.edit()
                            .putString(Constants.KEY_MODE, userSelectedMode)
                            .apply()
                    }
                    R.id.cycleToggleBtn -> {
                        showToast("Start Cycling")
                        userSelectedMode = "Cycling"
                        sharedPref.edit()
                            .putString(Constants.KEY_MODE, userSelectedMode)
                            .apply()
                    }
                }
            } else {
                if (toggleButtonGroup.checkedButtonId == View.NO_ID) {
                    showToast("No Item Selected")
                }
            }
        }

        Log.d("Inside oncreate1" , "Inside oncreate")
        subscribeToObservers()
        Log.d("Inside oncreate2" , "Inside oncreate")

//        Log.d("Inside subscribeToObservers" , "Inside subscribeToObservers")
//        TrackingService.showDialog.observe(viewLifecycleOwner, Observer {
//            Log.d("Inside showDialog1" , "Inside showDialog1")
//            updateShowDialog(it)
//            Log.d("Inside showDialog2" , "Inside showDialog2")
//        })

//        if(showDialog){
//            dialogToggle.show()
//        }

        cancelBtn.setOnClickListener {
            dialog.show()
        }
    }

    private fun showToast(str: String) {
        Toast.makeText(activity, str, Toast.LENGTH_SHORT).show()
        dialogToggle.dismiss()
    }

    private fun stopTrack(){

        sendCommandToService(ACTION_STOP_SERVICE)
    }

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            Log.d("Inside isTracking" , "Inside isTracking")
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addAllPolylines()
            moveCameraToUser()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner , Observer {
            curTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedShopWatchTime(curTimeInMillis , true)
            tvTimer.text = formattedTime
        })
    }

    private fun toggleRun(){
        if(isTracking){
            sendCommandToService(ACTION_PAUSE_SERVICE)
        }else{
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking : Boolean){
        this.isTracking = isTracking
        if(!isTracking){
            btnToggleStart.text = "Resume"
            btnFinishStart.visibility = View.VISIBLE
        }else{
            btnToggleStart.text = "Stop Tracking"
            btnFinishStart.visibility = View.GONE
        }
    }

    private fun moveCameraToUser() {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }

    private fun zoomToSeeWholeTrack(){
        val bounds = LatLngBounds.Builder()
        for(polyline in pathPoints){
            for(pos in polyline){
                bounds.include(pos)
            }
        }
        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                mapView.width,
                mapView.height,
                (mapView.height * 0.05f).toInt()
            )
        )
    }

    private fun endRunAndSaveToDb() {
        map?.snapshot { bmp ->
            val bitmap: Bitmap = bmp // Your bitmap image
            val sizeInBytes = bitmap.byteCount
            val sizeInKB = sizeInBytes / 1024
            val sizeInMB = sizeInKB / 1024
            Log.d("BitmapSize" , "{$sizeInMB MB}")
            Log.d("BitmapSize" , "{$sizeInKB KB}")
            println("Bitmap size: $sizeInMB MB")
            var distanceInMeters = 0
            for(polyline in pathPoints){
                distanceInMeters += TrackingUtility.calculatePolyLineLength(polyline).toInt()
            }

            val avgSpeed = round((distanceInMeters/1000f) / (curTimeInMillis / 1000f / 60 / 60) *10) /10f
            val avgSpeedMpm = kmhToMpm(avgSpeed)

            val dateTimestamp = Calendar.getInstance().timeInMillis

            val calendar = Calendar.getInstance()
            val weekNumber = calendar.get(Calendar.WEEK_OF_YEAR)

            //val caloriesBurned = calculateCaloriesBurned(weight.toFloat(), distanceInMeters.toFloat(), avgSpeedMpm, userSelectedMode).toInt()
            val caloriesBurned = ((distanceInMeters/1000f) * weight.toFloat()).toInt()
            val track = Track(bmp , dateTimestamp, avgSpeed , distanceInMeters , curTimeInMillis , caloriesBurned , weekNumber ,userSelectedMode)
            viewModel.insertTrack(track)
            Snackbar.make(
                requireActivity().findViewById(R.id.root_view),
                "Track Save Successfully",
                Snackbar.LENGTH_LONG
            ).show()
            stopTrack()
        }
    }

    private fun addAllPolylines() {
        for(polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if(pathPoints.isNotEmpty() && pathPoints.size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun sendCommandToService(action:String) =
        Intent(requireContext() , TrackingService::class.java).also{
            it.action = action
            requireContext().startService(it)
        }

    private fun onMoreButtonClicked() {
        setClickable(clicked)
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked
    }

    private fun setAnimation(clicked:Boolean) {
        if(!clicked){
            changeThemeBtn.startAnimation(fromBottom)
            cancelBtn.startAnimation(fromBottom)
            cancel_action_text.startAnimation(fromBottom)
            theme_action_text.startAnimation(fromBottom)
            moreBtn.startAnimation(rotateOpen)
        }else{
            changeThemeBtn.startAnimation(toBottom)
            cancelBtn.startAnimation(toBottom)
            cancel_action_text.startAnimation(toBottom)
            theme_action_text.startAnimation(toBottom)
            moreBtn.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked:Boolean) {
        if(!clicked){
            changeThemeBtn.visibility = View.VISIBLE
            cancelBtn.visibility = View.VISIBLE
            theme_action_text.visibility = View.VISIBLE
            cancel_action_text.visibility = View.VISIBLE
        }else{
            changeThemeBtn.visibility = View.INVISIBLE
            cancelBtn.visibility = View.INVISIBLE
            theme_action_text.visibility = View.INVISIBLE
            cancel_action_text.visibility = View.INVISIBLE
        }
    }

    private fun setClickable(clicked: Boolean){
        if(!clicked){
            changeThemeBtn.isClickable = true
            cancelBtn.isClickable = true
        }else{
            changeThemeBtn.isClickable = false
            cancelBtn.isClickable = false
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}

