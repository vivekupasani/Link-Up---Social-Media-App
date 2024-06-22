package com.vivekupasani.linkup

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vivekupasani.linkup.databinding.ActivityOnBoardOptionsBinding

class onBoardOptions : AppCompatActivity() {

    lateinit var binding: ActivityOnBoardOptionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnBoardOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGOLogIn.setOnClickListener {
            startActivity(Intent(this,LogInActivity::class.java))
            finish()
        }

        binding.btnGOSignUp.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }

    }
}