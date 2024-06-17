package com.droid.foodio

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.droid.foodio.databinding.ActivitySignUpBinding
import com.droid.foodio.utils.userModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {

    private lateinit var email:String
    private lateinit var password:String
    private lateinit var username:String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient : GoogleSignInClient


    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        // initialize Firebase Auth
        auth = Firebase.auth
        // initialize Firebase database
        database = Firebase.database.reference
        // initialize google Sign in
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.Alreadybtn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

        binding.createAccBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val username = binding.usernameEt.text.toString()

            if(email.isEmpty() || password.isEmpty() || username.isEmpty()){
                Toast.makeText(this, "please fill all the details", Toast.LENGTH_SHORT).show()
            }
            else{
                createAccount(email,password)
            }
        }

        binding.googleBtn.setOnClickListener {
                val intent = googleSignInClient.signInIntent
                launcher.launch(intent)

        }

    }

    private fun createAccount(email: String, password: String){
    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
        task ->
        if (task.isSuccessful) {
            Toast.makeText( this,  "Account Created successfully", Toast.LENGTH_SHORT).show()
            saveUserData()
           updateUi(auth.currentUser)
        }
            else{
            Toast.makeText( this,  "Account not created ", Toast.LENGTH_SHORT).show()
            Log.d("Account", "createUserWithEmail:failure", task.exception)
            }
        }
    }

    private fun saveUserData() {
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        username = binding.usernameEt.text.toString().trim()

        val user= userModel( username, email, password)
        val userId : String = FirebaseAuth.getInstance().currentUser!!.uid
        database.child(  "User").child(userId).setValue(user)
    }

    private val launcher= registerForActivityResult(ActivityResultContracts. StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount = task.result
                val credential: AuthCredential =
                    GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener{ authTask ->
                    if (authTask.isSuccessful) {
                        // successfully sign in with Google
                        Toast.makeText(this, "Successfully sign-in with Google", Toast.LENGTH_SHORT).show()
                        saveUserData()
                        updateUi(authTask.result?.user)
                        finish()
                    } else {
                        Toast.makeText(this, "Google Sign-in failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(this, "Google Sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    // Check if user is already logged in
    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent( this, MainActivity::class.java))
            finish()
        }
    }
}


