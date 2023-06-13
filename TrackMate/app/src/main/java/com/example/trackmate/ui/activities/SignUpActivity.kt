package com.example.trackmate.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.trackmate.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var googleSignUpClient: GoogleSignInClient
    private lateinit var authListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        firebaseAuth = FirebaseAuth.getInstance()

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                if (user.isEmailVerified) {
                    val intent = Intent(this, MainActivity::class.java);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "gpt", Toast.LENGTH_LONG).show()
                }
            }
        }
        FirebaseAuth.getInstance().addAuthStateListener(authListener)

        if(firebaseAuth.currentUser != null && firebaseAuth.currentUser?.isEmailVerified == true){
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignUpClient = GoogleSignIn.getClient(this , gso)

        btnGoogleSignUp.setOnClickListener {
            btnGoogleSignUp.visibility = View.INVISIBLE
            google_signup_progress_bar.visibility = View.VISIBLE
            signInGoogle()
        }

        btnSignup.setOnClickListener{

            btnSignup.visibility = View.INVISIBLE
            signup_progress_bar.visibility = View.VISIBLE

            val email = edEmail.text.toString()
            val password = edPassword.text.toString()
            val retryPass = edRetry.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && retryPass.isNotEmpty()){
                if(password == retryPass){
                    firebaseAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener {
                        if(it.isSuccessful){

                            firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                                Toast.makeText(this , "Verify Your Email" , Toast.LENGTH_LONG).show()
//                                FancyToast.makeText(this,"Verify Your Email",
//                                    FancyToast.LENGTH_SHORT,
//                                    FancyToast.INFO,true).show()
                            }?.addOnFailureListener {
                                Toast.makeText(this , it.toString() , Toast.LENGTH_LONG).show()
//                                FancyToast.makeText(this,it.toString(),
//                                    FancyToast.LENGTH_SHORT,
//                                    FancyToast.INFO,true).show()
                            }

                            updateUIaccount()

                        }else{
                            signup_progress_bar.visibility = View.INVISIBLE
                            btnSignup.visibility = View.VISIBLE
                            Toast.makeText(this , it.exception.toString() , Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    signup_progress_bar.visibility = View.INVISIBLE
                    btnSignup.visibility = View.VISIBLE
                    Toast.makeText(this , "Passwords are not matching" , Toast.LENGTH_LONG).show()
                }
            }else{
                signup_progress_bar.visibility = View.INVISIBLE
                btnSignup.visibility = View.VISIBLE
                Toast.makeText(this , "Empty Fields are not allowed" , Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUIaccount() {
        val intent = Intent(this , LoginActivity::class.java)
        signup_progress_bar.visibility = View.INVISIBLE
        btnSignup.visibility = View.VISIBLE
        startActivity(intent)
    }

    private fun signInGoogle() {
        val signUpIntent = googleSignUpClient.signInIntent
        launcher.launch(signUpIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){     // it checks the which account has been selected
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }else{
            btnGoogleSignUp.visibility = View.VISIBLE
            google_signup_progress_bar.visibility = View.INVISIBLE
//            FancyToast.makeText(this,"Email Id was not selected",
//                FancyToast.LENGTH_SHORT,
//                FancyToast.INFO,true).show()

            Toast.makeText(this , "Email Id was not selected" , Toast.LENGTH_LONG).show()
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if(account != null){
                updateUI(account)
            }
        }else{
            btnGoogleSignUp.visibility = View.VISIBLE
            google_signup_progress_bar.visibility = View.INVISIBLE
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                val intent = Intent(this , MainActivity::class.java)
                startActivity(intent)
//                FancyToast.makeText(this,"Successfully Logged In",
//                    FancyToast.LENGTH_LONG,
//                    FancyToast.SUCCESS,true).show()

                Toast.makeText(this , "Successfully Logged In" , Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(firebaseAuth.currentUser?.isEmailVerified == true){
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
        }
    }

    override fun onStart() {
        super.onStart()

//        if(firebaseAuth.currentUser != null){
//            val intent = Intent(this, DialogActivity::class.java)
//            startActivity(intent)
//        }
//        if( firebaseAuth.currentUser?.isEmailVerified == true){
//            val intent = Intent(this, DialogActivity::class.java);
//            startActivity(intent);
//        }

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                if (user.isEmailVerified) {
                    val intent = Intent(this, MainActivity::class.java);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "gpt", Toast.LENGTH_LONG).show()
                }
            }
        }
        FirebaseAuth.getInstance().addAuthStateListener(authListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().removeAuthStateListener(authListener)
    }
}