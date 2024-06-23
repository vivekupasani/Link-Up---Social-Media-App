package com.vivekupasani.linkup

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.vivekupasani.linkup.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var previousItemId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.dark(Color.TRANSPARENT))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.framelayout.id) as NavHostFragment

        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        // Initialize the previousItemId with the default selected item
        previousItemId = binding.bottomNavigationView.selectedItemId;

        // Custom Navigation Logic
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.addPost) {
                // Handle the menu item to open Activity
                val intent = Intent(
                    this@MainActivity,
                    AddPostActivity::class.java
                )
                startActivity(intent)
                binding.bottomNavigationView.menu.findItem(previousItemId).setChecked(true);
                false // Return false to avoid changing the selected item
            } else {

                // Update the previousItemId before navigation
                previousItemId = item.itemId;

                // For other items, let NavController handle them
                (onNavDestinationSelected(item, navController)
                        || super.onOptionsItemSelected(item))
            }
        }

    }

}
