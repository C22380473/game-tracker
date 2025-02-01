package com.example.gametrackerapp

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gametrackerapp.databinding.ActivityMainBinding

// Reference: The following code is from AndroidStudio's built-in Bottom Navigation Views Activity Template
// It has been customised to fit the requirements of the Game Tracker app.

class MainActivity : AppCompatActivity() {

    // View binding to access views in the layout file
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout and get binding object
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // Set the root view of the layout

        // Get reference to the BottomNavigationView from the layout file
        val navView: BottomNavigationView = binding.navView

        // Find the NavController which is responsible for handling navigation
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Define top-level destinations (menu items) for the Adapter
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_mygames, R.id.navigation_inprogress, R.id.navigation_completed, R.id.navigation_favorites,
                R.id.navigation_wishlist
            )
        )
        // Sets up ActionBar title to change between destinations
        setupActionBarWithNavController(navController, appBarConfiguration)
        // Sets up BottomNavigationView to automatically handle item selection and fragment swapping
        navView.setupWithNavController(navController)
    }
}
// Reference complete