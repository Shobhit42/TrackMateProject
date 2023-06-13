package com.example.trackmate.ui.bottomsheets

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.trackmate.R
import com.example.trackmate.others.SortType
import com.example.trackmate.ui.activities.LoginActivity
import com.example.trackmate.ui.activities.SignUpActivity
import com.example.trackmate.ui.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*

class BottomSheetsFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet , container , false);
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        close_bottomsheet.setOnClickListener {
            dismiss();
        }

        settingsBtn.setOnClickListener {
            findNavController().navigate(
                R.id.settingsFragment
            )
        }

        settingsIcon.setOnClickListener {
            findNavController().navigate(
                R.id.settingsFragment
            )
        }

        tvLogout.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        ivLogout.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}