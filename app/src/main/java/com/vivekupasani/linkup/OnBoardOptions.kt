package com.vivekupasani.linkup

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.vivekupasani.linkup.databinding.ActivityOnBoardOptionsBinding

class OnBoardOptions : AppCompatActivity() {

    lateinit var binding: ActivityOnBoardOptionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.dark(Color.TRANSPARENT))
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