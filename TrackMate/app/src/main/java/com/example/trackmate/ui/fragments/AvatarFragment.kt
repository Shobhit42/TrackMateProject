package com.example.trackmate.ui.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString

import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.trackmate.R
import com.example.trackmate.others.Constants
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_avatar.*
import kotlinx.android.synthetic.main.fragment_setup.*
import java.io.ByteArrayOutputStream
import java.security.spec.PSSParameterSpec.DEFAULT
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class AvatarFragment : Fragment(R.layout.fragment_avatar) {

    @Inject
    lateinit var sharedPref : SharedPreferences


    @field:Named("Second")
    @JvmField
    @Inject
    var isFirstAppOpenAvatar = true

    private var selectedImg = "";

    lateinit var bitmap : Bitmap

    @SuppressLint("ResourceAsColor", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isFirstAppOpenAvatar){
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.avatarFragment , true)
                .build()
            findNavController().navigate(
                R.id.action_avatarFragment_to_setupFragment,
                savedInstanceState,
                navOptions
            )
        }

        floatingActionButton2.setOnClickListener{
            val success = writePersonalDataToSharedPref()
            if(success){
                findNavController().navigate(R.id.action_avatarFragment_to_setupFragment)
            }else{
                Snackbar.make(requireView(),"Please select a image" , Snackbar.LENGTH_SHORT).show()
            }
        }

        female1.setOnClickListener {
            makeImageViewNormal()
            female1.scaleX = 1.2F
            female1.scaleY = 1.2F
            selectedImg = "female1"
            bitmap = (female1.drawable as BitmapDrawable).bitmap
        }

        female2.setOnClickListener {
            makeImageViewNormal()
            female2.scaleX = 1.2F
            female2.scaleY = 1.2F
            selectedImg = "female2"
            bitmap = (female2.drawable as BitmapDrawable).bitmap
        }

        female3.setOnClickListener {
            makeImageViewNormal()
            female3.scaleX = 1.2F
            female3.scaleY = 1.2F
            selectedImg = "female3"
            bitmap = (female3.drawable as BitmapDrawable).bitmap
        }

        male1.setOnClickListener {
            makeImageViewNormal()
            male1.scaleX = 1.2F
            male1.scaleY = 1.2F
            selectedImg = "male1"
            bitmap = (male1.drawable as BitmapDrawable).bitmap
        }

        male2.setOnClickListener {
            makeImageViewNormal()
            male2.scaleX = 1.2F
            male2.scaleY = 1.2F
            selectedImg = "male2"
            bitmap = (male2.drawable as BitmapDrawable).bitmap
        }

        male3.setOnClickListener {
            makeImageViewNormal()
            male3.scaleX = 1.2F
            male3.scaleY = 1.2F
            selectedImg = "male3"
            bitmap = (male3.drawable as BitmapDrawable).bitmap
        }
    }

    private fun makeImageViewNormal() {
        female1.scaleX = 1F
        female1.scaleY = 1F

        female2.scaleX = 1F
        female2.scaleY = 1F

        female3.scaleX = 1F
        female3.scaleY = 1F

        male1.scaleX = 1F
        male1.scaleY = 1F

        male2.scaleX = 1F
        male2.scaleY = 1F

        male3.scaleX = 1F
        male3.scaleY = 1F
    }

    private fun writePersonalDataToSharedPref() : Boolean {
        if(selectedImg.isEmpty()){
            return false;
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64String = android.util.Base64.encodeToString(byteArray, Base64.DEFAULT)
        sharedPref.edit()
            .putString(Constants.KEY_IMG_NUMBER , selectedImg)
            .putString(Constants.KEY_IMG, base64String)
            .putBoolean(Constants.KEY_FIRST_TIME_TOGGLE_AVATAR, false)
            .apply()

        return true
    }

}