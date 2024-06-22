package com.vivekupasani.linkup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.vivekupasani.linkup.databinding.ActivityMainBinding
import com.vivekupasani.linkup.fragments.Home
import com.vivekupasani.linkup.fragments.Profile
import com.vivekupasani.linkup.fragments.Search

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the default fragment to Home when the app opens
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.framelayout, Home())
                .commit()
        }

        // Fragment changing from navBar
        replaceFragment()



    }

    private fun replaceFragment() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            val transaction = supportFragmentManager.beginTransaction()
            when (it.itemId) {
                R.id.home -> {
                    transaction.replace(R.id.framelayout, Home())
                }

                R.id.search -> {
                    transaction.replace(R.id.framelayout, Search())
                }

                R.id.add -> {
                    startActivity(Intent(this,addPostActivity::class.java))
                }

                R.id.notification -> {
                    Toast.makeText(this, "Clicked notification", Toast.LENGTH_SHORT).show()
                }

                R.id.profile -> {
                    transaction.replace(R.id.framelayout, Profile())
                }
            }
            transaction.commit()
            true
        }
    }
}
