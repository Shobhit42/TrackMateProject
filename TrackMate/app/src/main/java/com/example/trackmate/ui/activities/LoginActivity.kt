package com.example.trackmate.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import com.example.trackmate.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var authListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)

        btnGoogleLogin.setOnClickListener{
            btnGoogleLogin.visibility = View.INVISIBLE
            google_progress_bar.visibility = View.VISIBLE
            signInGoogle()
        }

        textView5.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java);
            startActivity(intent);
        }

        btnLogin.setOnClickListener {

            val email = edEmail.text.toString();
            val password = edPassword.text.toString();

            if (email.isNotEmpty() && password.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        if(firebaseAuth.currentUser?.isEmailVerified == true){
                            Toast.makeText(this, "user is verified", Toast.LENGTH_LONG).show();
                            val intent = Intent(this, MainActivity::class.java);
                            startActivity(intent);
                        }else{
                            Toast.makeText(this, "verify your email", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show();
                    }
                }

            } else {
                Toast.makeText(this, "Empty Fields are not allowed", Toast.LENGTH_LONG).show();

            }
        }

    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){     // it checks the which account has been selected
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }else{
            btnGoogleLogin.visibility = View.VISIBLE
            google_progress_bar.visibility = View.INVISIBLE
            Toast.makeText(this, "Email Id was not selected", Toast.LENGTH_SHORT).show()
                //FancyToast.makeText(this,"Email Id was not selected",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if(account != null){
                updateUI(account)
            }
        }else{
            btnGoogleLogin.visibility = View.VISIBLE
            google_progress_bar.visibility = View.INVISIBLE
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                val intent = Intent(this , MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this , "Successfully Logged In" , Toast.LENGTH_LONG).show()
                //FancyToast.makeText(this,"Successfully Logged In",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show();
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