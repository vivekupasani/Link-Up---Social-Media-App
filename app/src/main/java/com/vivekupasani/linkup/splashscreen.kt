package com.vivekupasani.linkup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.vivekupasani.linkup.databinding.ActivitySplashscreenBinding

class splashscreen : AppCompatActivity() {

    lateinit var binding: ActivitySplashscreenBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        Handler().postDelayed({
            val currentUser = auth.currentUser
            if (currentUser != null){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, onBoardOptions::class.java))
                finish()
            }

        },3000)
    }
}