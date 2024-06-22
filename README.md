LinkUp is an Android social networking application that allows users to share posts, like posts, and navigate through different sections such as Home, Search, Add Post, Notifications, and Profile.

• Table of Contents
  - [Features](#features)
  - [Installation](#installation)
  - [Usage](#usage)
  - [Navigation](#navigation)

• Features
  - User authentication (Sign Up, Sign In, Sign Out)
  - Home feed with posts from all users
  - Search functionality to find other users
  - Add new posts with images and captions
  - Like and Unlike posts
  - View user profiles
  - Notification placeholder

• Installation

1. Clone the repository
    ```bash
    git clone https://github.com/vivekupasani/LinkUp.git
    cd LinkUp
    ```

2. Open the project in Android Studio
    - Open Android Studio.
    - Click on `File -> Open` and select the project directory.

3. Build the project
    - Let Android Studio download and install the required dependencies.
    - Build the project by clicking on `Build -> Rebuild Project`.

4. Run the project
    - Connect your Android device or start an emulator.
    - Click on the `Run` button in Android Studio.

• Usage

1. Sign Up or Sign In
    - Launch the app and sign up for a new account or sign in with an existing account.

2. Home Feed
    - View posts from other users in the home feed.

3. Search Users
    - Use the search functionality to find and view other user profiles.

4. Add New Post
    - Click on the `Add` button in the bottom navigation bar to add a new post with an image and caption.

5. Like Posts
    - Like or unlike posts by clicking the like button on the posts in the home feed.

6. View Profiles
    - Click on usernames to view their profiles.

• Navigation

The app uses Android Jetpack Navigation component for seamless navigation between different sections. Here's a brief overview of the navigation setup:

1. Navigation Host
    
    The `NavHostFragment` is used as the primary navigation host in `activity_main.xml`:

    ```xml
    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_marginBottom="?attr/actionBarSize"
        android:name="androidx.navigation.fragment.NavHostFragment"
        app:defaultNavHost="true"
        app:navGraph="@navigation/nav_graph" />
    ```

2. Navigation Graph

    The navigation graph (`nav_graph.xml`) defines the navigation paths and actions:

    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <navigation xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        app:startDestination="@id/homeFragment">

        <fragment
            android:id="@+id/homeFragment"
            android:name="com.vivekupasani.linkup.fragments.Home"
            tools:layout="@layout/fragment_home" />

        <fragment
            android:id="@+id/searchFragment"
            android:name="com.vivekupasani.linkup.fragments.Search"
            tools:layout="@layout/fragment_search" />

        <fragment
            android:id="@+id/profileFragment"
            android:name="com.vivekupasani.linkup.fragments.Profile"
            tools:layout="@layout/fragment_profile" />
    </navigation>
    ```

3. MainActivity

    The `MainActivity` sets up the `NavController` and handles navigation actions:

    ```kotlin
    package com.vivekupasani.linkup

    import android.content.Intent
    import android.os.Bundle
    import android.widget.Toast
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.navigation.NavController
    import androidx.navigation.fragment.NavHostFragment
    import androidx.navigation.ui.setupWithNavController
    import com.vivekupasani.linkup.databinding.ActivityMainBinding

    class MainActivity : AppCompatActivity() {

        lateinit var binding: ActivityMainBinding
        private lateinit var navController: NavController

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Set up the NavController
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController

            // Set up BottomNavigationView with NavController
            binding.bottomNavigationView.setupWithNavController(navController)

            // Handle the Add button separately as it's not a fragment
            binding.bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.add -> {
                        startActivity(Intent(this, addPostActivity::class.java))
                        true
                    }
                    R.id.notification -> {
                        Toast.makeText(this, "Clicked notification", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> {
                        NavigationUI.onNavDestinationSelected(item, navController)
                    }
                }
            }
        }
    }
