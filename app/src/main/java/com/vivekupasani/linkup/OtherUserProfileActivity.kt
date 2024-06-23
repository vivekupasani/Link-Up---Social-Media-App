package com.vivekupasani.linkup

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.vivekupasani.linkup.databinding.ActivityOtherUserProfileBinding
import com.vivekupasani.linkup.models.UserModel

class OtherUserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtherUserProfileBinding

    private lateinit var fireRef: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userList: ArrayList<UserModel>

    private lateinit var mail: String
    private lateinit var pass: String
    private lateinit var profile: String
    private var clickedUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.dark(Color.TRANSPARENT))
        binding = ActivityOtherUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickedUserId = intent.getStringExtra("userId")

        auth = FirebaseAuth.getInstance()
        fireRef = FirebaseFirestore.getInstance()
        userList = ArrayList()

        // Edit profile button click listener
        binding.btnEditprofile.setOnClickListener {
            val eName = binding.profileName.text.toString()
            val eProfession = binding.profileProfession.text.toString()
            val eBio = binding.profileBio.text.toString()
            val intent = Intent(applicationContext, editProfile::class.java)
            intent.putExtra("name", eName)
            intent.putExtra("profession", eProfession)
            intent.putExtra("bio", eBio)
            intent.putExtra("email", mail)
            intent.putExtra("password", pass)
            intent.putExtra("profilepic", profile)
            startActivity(intent)
        }

        binding.logOut.setOnClickListener {
            AlertDialog.Builder(applicationContext).apply {
                setMessage("Log out of your account?")
                setPositiveButton("Log out") { dialog, which ->
                    auth.signOut()
                    startActivity(Intent(applicationContext, LogInActivity::class.java))
                    finish()
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
        }

        binding.navigateBack.setOnClickListener {
            // Close the activity and remove it from back stack
            finish()
        }

        if(clickedUserId != null) {
            loadUserProfile(clickedUserId!!)
        }

    }

    private fun loadUserProfile(userId: String) {
        setProgressBar(true)
        fireRef.collection("Users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d("Profile", "DocumentSnapshot data: ${document.data}")
                    val user = document.toObject(UserModel::class.java)
                    Log.d("Profile", "User: ${user}")
                    if (user != null) {

                        // Update UI with user data
                        binding.profileName.text = user.name
                        binding.profileProfession.text = user.proffesion
                        binding.profileBio.text = user.bio
                        binding.profileFollowers.text = user.follower.toString()
                        binding.profileFollowing.text = user.following.toString()
                        binding.profilePost.text = user.post.toString()

                        Picasso.get().load(user.profilePic).noFade().into(binding.profileImage)

                        mail = user.email.toString()
                        pass = user.password.toString()
                        profile = user.profilePic.toString()
                        // Set other user details here
                    } else {
                        Log.d("Profile", "User data is null")
                    }
                } else {
                    Log.d("Profile", "No such document")
                }
                setProgressBar(false)
            }
            .addOnFailureListener { exception ->
                Log.w("Profile", "Error getting document.", exception)
                setProgressBar(false)
            }
    }

    private fun setProgressBar(inProgress: Boolean) {
        binding.progressBar.isVisible = inProgress
    }

}