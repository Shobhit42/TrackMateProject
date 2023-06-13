package com.example.trackmate.ui.fragments

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.trackmate.R
import com.example.trackmate.db.*
import com.example.trackmate.db.api.posts.postsmodel.CreateNewPost
import com.example.trackmate.db.api.posts.postsmodel.Post
import com.example.trackmate.db.api.posts.services.PostService
import com.example.trackmate.db.api.ServiceBuilder
import com.example.trackmate.others.Constants
import com.example.trackmate.others.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    @Inject
    lateinit var sharedPref : SharedPreferences

    private lateinit var selectedTrack: Track
    private var postImage: Bitmap? = null
    private var formattedTime = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        // Retrieve the selectedTrack from the arguments bundle
        selectedTrack = arguments?.getParcelable<Track>("selectedTrack")!!

        val formattedDate = TrackingUtility.formatDate(selectedTrack.timestamp)
        view.dateEd.text = formattedDate
        view.modeEd.text = selectedTrack.mode
        view.edCalorieBurned.text = "${ selectedTrack.caloriesBurned } Kcal"

        view.edAvgSpeed.text = "${ selectedTrack.avgSpeedInKMH } Km/h"

        val formattedDistance = TrackingUtility.formatDistance(selectedTrack.distanceInMeter)
        view.edDistance.text = formattedDistance

        formattedTime = TrackingUtility.formatTime(selectedTrack.timeInMillis)
        view.edDuration.text = formattedTime

        view.mapImageOfItem.setImageBitmap(selectedTrack.img)
        postImage = selectedTrack.img

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calorieProgress.progress = selectedTrack.caloriesBurned
        speedProgress.progress = selectedTrack.avgSpeedInKMH.toInt()

        var finalDistance = selectedTrack.distanceInMeter
        finalDistance /= 1000
        distanceProgress.progress = finalDistance

        val finalDuration = selectedTrack.timeInMillis / 60000
        durationProgress.progress = finalDuration.toInt()

        createPost.setOnClickListener {

            val byteArrayOutputStream = ByteArrayOutputStream()
            postImage?.compress(Bitmap.CompressFormat.PNG, 5, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val base64String = android.util.Base64.encodeToString(byteArray, Base64.DEFAULT)

            val userProfile = sharedPref.getString(Constants.KEY_IMG_NUMBER, "")!!
            val userId = sharedPref.getString(Constants.KEY_USER_ID, "")!!
            val userName = sharedPref.getString(Constants.KEY_NAME, "")!!
            val timeInMillisInLong : Long = Calendar.getInstance().timeInMillis

            val newPost = CreateNewPost("${ selectedTrack.avgSpeedInKMH } Km/h",
                "${ selectedTrack.caloriesBurned } Kcal",
                formattedTime,"0",base64String,userId,userProfile,userName, timeInMillisInLong.toString())

            val postService: PostService = ServiceBuilder.buildService(PostService::class.java)
            val requestCall: Call<Post> = postService.createPost(newPost)

            requestCall.enqueue(object : Callback<Post>{
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if(response.isSuccessful){
                        Toast.makeText(requireContext() , "Success" , Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext() , "Not added" , Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    Toast.makeText(requireContext() , "Some error" , Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    fun formatTime1(millis: Long): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        val decimalMinutes = String.format("%.1f", minutes + seconds / 60.0)
        return decimalMinutes
    }

    fun formatDistance1(meters: Int): String {
        val kilometers = meters / 1000
        val metersRemainder = meters % 1000
        return if (metersRemainder == 0) {
            "$kilometers"
        } else {
            String.format("%d.%03d", kilometers, metersRemainder)
        }
    }

}