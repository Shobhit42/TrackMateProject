package com.example.trackmate.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.trackmate.R
import com.example.trackmate.db.api.ServiceBuilder
import com.example.trackmate.db.api.createuser.services.UserService
import com.example.trackmate.db.api.createuser.usermodel.CreateUserRequest
import com.example.trackmate.db.api.createuser.usermodel.CreateUserResponse
import com.example.trackmate.others.Constants
import com.example.trackmate.others.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.trackmate.others.Constants.KEY_GENDER
import com.example.trackmate.others.Constants.KEY_NAME
import com.example.trackmate.others.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_setup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    @Inject
    lateinit var sharedPref : SharedPreferences


    @field:Named("First")
    @JvmField
    @Inject
    var isFirstAppOpener = true

    private var weightSelected = 0
    private var genderSelected : String = "";

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isFirstAppOpener){
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment , true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_profileFragment,
                savedInstanceState,
                navOptions
            )
        }

        btnContinue.setOnClickListener{
            val success = writePersonalDataToSharedPref()
            if(success){
                val selectedImg = sharedPref.getString(Constants.KEY_IMG_NUMBER, "");
                val userName = sharedPref.getString(Constants.KEY_NAME, "");
                Log.d("SetUpFragment" , "$userName")
                if (selectedImg != null) {
                    if (userName != null) {
                        makeRequest(selectedImg,userName)
                    }
                };
                findNavController().navigate(R.id.action_setupFragment_to_profileFragment)
            }else{
                Snackbar.make(requireView(),"Please enter all the fields" , Snackbar.LENGTH_SHORT).show()
            }
        }

        Male_toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // set the male icon with white color
                Male_toggle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.male_icon_white, 0, 0, 0)

                // set the text color to white
                Male_toggle.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                genderSelected = "Male";
            } else {
                // set the male icon with black color
                Male_toggle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.male_icon, 0, 0, 0)

                // set the text color to black
                Male_toggle.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }

        offer.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // set the male icon with white color
                offer.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.female_icon, 0, 0, 0)

                // set the text color to white
                offer.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                genderSelected = "Female"
            } else {
                // set the male icon with black color
                offer.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.female_icon_black, 0, 0, 0)

                // set the text color to black
                offer.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }

        ruler_picker1.selectValue(60);
        ruler_picker1.setValuePickerListener(object : RulerValuePickerListener {
            override fun onValueChange(value: Int) {
                weightSelected = value
                //Value changed and the user stopped scrolling the ruler.
                //You can consider this value as final selected value.
                finalWeight.text = "$value Kg"
            }

            override fun onIntermediateValueChange(selectedValue: Int) {
                //Value changed but the user is still scrolling the ruler.
                //This value is not final value. You can utilize this value to display the current selected value.
                finalWeight.text = "$selectedValue Kg"
            }
        })
    }

    private fun makeRequest(selectedImg : String , name : String) {

        val newUser = CreateUserRequest(name , selectedImg)

        val userService: UserService = ServiceBuilder.buildService(UserService::class.java)
        val requestCall: Call<CreateUserResponse> = userService.createUser("https://track-mate-backend-app.onrender.com/api/user/createUser",newUser)

        requestCall.enqueue(object : Callback<CreateUserResponse> {
            override fun onResponse(
                call: Call<CreateUserResponse>,
                response: Response<CreateUserResponse>
            ) {
                if(response.isSuccessful){
                    var newlyCreatedUser : CreateUserResponse? = response.body();
                    val userId = newlyCreatedUser?.user?._id
                    sharedPref.edit()
                        .putString(Constants.KEY_USER_ID , userId)
                        .apply()
                    Toast.makeText(requireContext(), "SuccessFully User Created ${userId}" , Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(requireContext(), " Failed to add user" , Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CreateUserResponse>, t: Throwable) {
                Toast.makeText(requireContext(), " Failed to add" , Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun writePersonalDataToSharedPref() : Boolean {
        val name = edName.text.toString()
        if(weightSelected.toString().isEmpty() || name.isEmpty()){
            return false;
        }
        sharedPref.edit()
            .putString(KEY_NAME , name)
            .putInt(KEY_WEIGHT, weightSelected)
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .putString(KEY_GENDER, genderSelected)
            .apply()

        return true
    }
}