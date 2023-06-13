package com.example.trackmate.ui.fragments

import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.trackmate.R
import com.example.trackmate.others.Constants
import com.example.trackmate.others.Constants.KEY_IMG
import com.example.trackmate.others.Constants.KEY_NAME
import com.example.trackmate.others.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_setup.*
import kotlinx.android.synthetic.main.fragment_setup.ruler_picker1
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPref : SharedPreferences

    private var first : Boolean = true;
    private var weightSelected = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        editBtn2.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_avatarEditFragment)
        }

        loadFieldsFromSharedPref()
        btnApplyChanges.setOnClickListener {
            val success = applyChangesToSharedPref()
            if(success){
                Snackbar.make(view, "Saved changes" , Snackbar.LENGTH_LONG).show()
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.settingsFragment , true)
                    .build()
                findNavController().navigate(R.id.action_settingsFragment_to_profileFragment2 , savedInstanceState , navOptions)
            }else{
                Snackbar.make(view, "Please Fill Out All the Fields" , Snackbar.LENGTH_LONG).show()
            }
        }

        ruler_picker2.setValuePickerListener(object : RulerValuePickerListener {
            override fun onValueChange(value: Int) {
                if(!first){
                    val mediaPlayer = MediaPlayer.create(
                        context,
                        R.raw.scale)
                    mediaPlayer.start()
                }
                first = !first
                //Value changed and the user stopped scrolling the ruler.
                //You can consider this value as final selected value.
                edFinalWeight.text = "$value Kg"
                weightSelected = value
            }

            override fun onIntermediateValueChange(selectedValue: Int) {
                //Value changed but the user is still scrolling the ruler.
                //This value is not final value. You can utilize this value to display the current selected value.
                edFinalWeight.text = "$selectedValue Kg"
            }
        })
    }

    private fun loadFieldsFromSharedPref() {
        val name = sharedPref.getString(KEY_NAME, "");
        val weight = sharedPref.getInt(KEY_WEIGHT, 80)
        val base64String = sharedPref.getString(KEY_IMG, "");
        val byteArray = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        ivProfileImage.setImageBitmap(bitmap)
        edNameSettings.setText(name)
        val weightString = weight.toString()
        edFinalWeight.text = "$weightString Kg"
        ruler_picker2.selectValue(weight);
    }

    private fun applyChangesToSharedPref() : Boolean {
        val name = edNameSettings.text.toString()
        if(weightSelected.toString().isEmpty() || name.isEmpty()){
            return false;
        }
        sharedPref.edit()
            .putString(Constants.KEY_NAME, name)
            .putInt(Constants.KEY_WEIGHT, weightSelected)
            .apply()

        return true
    }

}