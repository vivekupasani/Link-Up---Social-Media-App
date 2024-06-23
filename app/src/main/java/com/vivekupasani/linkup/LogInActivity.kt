package com.vivekupasani.linkup

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.vivekupasani.linkup.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth

    lateinit var binding: ActivityLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.dark(Color.TRANSPARENT))
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.goSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.btnLogIn.setOnClickListener {
            logIn()
        }
    }

    private fun logIn() {
        val mail = binding.etEmailIN.text.toString()
        val pass = binding.etPasswordIN.text.toString()

        if (mail.isEmpty() || pass.isEmpty()){
            Toast.makeText(this,"Fill the info",Toast.LENGTH_SHORT).show()
        }else{
            setProgressbar(true)
            auth.signInWithEmailAndPassword(mail,pass)
                .addOnSuccessListener {
                    setProgressbar(false)
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
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