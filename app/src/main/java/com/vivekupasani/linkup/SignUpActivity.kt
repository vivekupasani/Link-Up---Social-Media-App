package com.vivekupasani.linkup

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vivekupasani.linkup.databinding.ActivitySignUpBinding
import com.vivekupasani.linkup.models.UserModel

class SignUpActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var fireRef: FirebaseFirestore

    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.dark(Color.TRANSPARENT))
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        fireRef = FirebaseFirestore.getInstance()

        binding.goLogin.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        binding.btnSignUp.setOnClickListener {
            signUp()
        }

    }

    private fun signUp() {
        val email = binding.etEmailUP.text.toString()
        val password = binding.etPasswordUP.text.toString()
        val rePass = binding.etRePassUP.text.toString()
        val name = binding.etNameUP.text.toString()

        //checking edit texts
        if (name.isEmpty()) {
            binding.etNameUP.setError("Enter name ")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailUP.setError("Enter valid email")
            return
        }

        if (password.length < 6) {
            binding.etPasswordUP.setError("Password must be 6 characters")
            return
        }

        if (rePass != password) {
            binding.etRePassUP.setError("Password not matching")
            return
        }
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || rePass.isEmpty()) {
            Toast.makeText(this, "Fill all info", Toast.LENGTH_SHORT).show()
        } else {
            setProgressbar(true)
            //sign up with auth
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {

                //storing data into firestore
                fireRef.collection("Users")
                    .document(auth.currentUser!!.uid)
                    .set(UserModel(auth.currentUser!!.uid, name, email, password))
                    .addOnSuccessListener {
                        setProgressbar(false)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        setProgressbar(false)
                        Toast.makeText(this, "Failed to Sign up", Toast.LENGTH_SHORT).show()
                    }
            }
        }


    }

    fun setProgressbar(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}