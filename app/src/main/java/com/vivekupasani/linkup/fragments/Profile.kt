package com.vivekupasani.linkup.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.vivekupasani.linkup.LogInActivity
import com.vivekupasani.linkup.databinding.FragmentProfileBinding
import com.vivekupasani.linkup.editProfile
import com.vivekupasani.linkup.models.userModel

class Profile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var fireRef: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userList: ArrayList<userModel>

    lateinit var mail: String
    lateinit var pass: String
    lateinit var profile: String
    private var clickedUserId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            clickedUserId = it.getString("currentUserUID")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        fireRef = FirebaseFirestore.getInstance()
        userList = ArrayList()

        val userId = auth.currentUser!!.uid


        // Fetch user details from database
        if (clickedUserId == userId){
            loadUserProfile(userId)
        }else{
            currentUserProfile(clickedUserId)
        }


        // Edit profile button click listener
        binding.btnEditprofile.setOnClickListener {
            val eName = binding.profileName.text.toString()
            val eProfession = binding.profileProfession.text.toString()
            val eBio = binding.profileBio.text.toString()
            val intent = Intent(context, editProfile::class.java)
            intent.putExtra("name", eName)
            intent.putExtra("profession", eProfession)
            intent.putExtra("bio", eBio)
            intent.putExtra("email", mail)
            intent.putExtra("password", pass)
            intent.putExtra("profilepic", profile)
            startActivity(intent)
        }

        binding.actionBar.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                .setMessage("Log out of your account?")
                .setPositiveButton("Log out",DialogInterface.OnClickListener { dialog, which ->
                    auth.signOut()
                    startActivity(Intent(requireContext(),LogInActivity::class.java))
                    activity?.finish()
                })
                .setNegativeButton("Cancel",DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                .show()
        }
    }

    private fun currentUserProfile(clickedUserId: String?) {
        setProgressBar(true)
        fireRef.collection("Users")
            .document(clickedUserId!!)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d("Profile", "DocumentSnapshot data: ${document.data}")
                    val user = document.toObject(userModel::class.java)
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

    private fun loadUserProfile(userId: String) {
        setProgressBar(true)
        fireRef.collection("Users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d("Profile", "DocumentSnapshot data: ${document.data}")
                    val user = document.toObject(userModel::class.java)
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
        if(inProgress){
            binding.progressBar.isVisible = true
            binding.actionBar.isVisible = false
        }else{
            binding.progressBar.isVisible = false
            binding.actionBar.isVisible = true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
