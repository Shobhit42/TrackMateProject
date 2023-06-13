package com.example.trackmate.ui.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.trackmate.R
import com.example.trackmate.others.Constants
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_avatar.*
import kotlinx.android.synthetic.main.fragment_avatar_edit.*
import kotlinx.android.synthetic.main.fragment_setup.*
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class AvatarEditFragment : Fragment(R.layout.fragment_avatar_edit) {

    @Inject
    lateinit var sharedPref : SharedPreferences

    private var selectedImg = "";

    lateinit var bitmap : Bitmap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnApplyChangesAvatar.setOnClickListener{
            val success = editPersonalDataToSharedPref()
            if(success){
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.avatarEditFragment , true)
                    .build()
                findNavController().navigate(R.id.action_avatarEditFragment_to_settingsFragment , savedInstanceState , navOptions)
            }else{
                Snackbar.make(requireView(),"Please select a image" , Snackbar.LENGTH_SHORT).show()
            }
        }

        female1Edit.setOnClickListener {
            makeImageViewNormal()
            female1Edit.scaleX = 1.2F
            female1Edit.scaleY = 1.2F
            selectedImg = "female1Edit"
            bitmap = (female1Edit.drawable as BitmapDrawable).bitmap
        }

        female2Edit.setOnClickListener {
            makeImageViewNormal()
            female2Edit.scaleX = 1.2F
            female2Edit.scaleY = 1.2F
            selectedImg = ""
            bitmap = (female2Edit.drawable as BitmapDrawable).bitmap
        }

        female3Edit.setOnClickListener {
            makeImageViewNormal()
            female3Edit.scaleX = 1.2F
            female3Edit.scaleY = 1.2F
            selectedImg = "female3Edit"
            bitmap = (female3Edit.drawable as BitmapDrawable).bitmap
        }

        male1Edit.setOnClickListener {
            makeImageViewNormal()
            male1Edit.scaleX = 1.2F
            male1Edit.scaleY = 1.2F
            selectedImg = "male1Edit"
            bitmap = (male1Edit.drawable as BitmapDrawable).bitmap
        }

        male2Edit.setOnClickListener {
            makeImageViewNormal()
            male2Edit.scaleX = 1.2F
            male2Edit.scaleY = 1.2F
            selectedImg = "male2Edit"
            bitmap = (male2Edit.drawable as BitmapDrawable).bitmap
        }

        male3Edit.setOnClickListener {
            makeImageViewNormal()
            male3Edit.scaleX = 1.2F
            male3Edit.scaleY = 1.2F
            selectedImg = "male3"
            bitmap = (male3Edit.drawable as BitmapDrawable).bitmap
        }
    }

    private fun makeImageViewNormal() {
        female1Edit.scaleX = 1F
        female1Edit.scaleY = 1F

        female2Edit.scaleX = 1F
        female2Edit.scaleY = 1F

        female3Edit.scaleX = 1F
        female3Edit.scaleY = 1F

        male1Edit.scaleX = 1F
        male1Edit.scaleY = 1F

        male2Edit.scaleX = 1F
        male2Edit.scaleY = 1F

        male3Edit.scaleX = 1F
        male3Edit.scaleY = 1F
    }

    private fun editPersonalDataToSharedPref() : Boolean {
        if(selectedImg.isEmpty()){
            return false;
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64String = android.util.Base64.encodeToString(byteArray, Base64.DEFAULT)
        sharedPref.edit()
            .putString(Constants.KEY_IMG, base64String)
            .putString(Constants.KEY_IMG_NUMBER, selectedImg)
            .apply()

        return true
    }

}