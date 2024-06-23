package com.vivekupasani.linkup.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.vivekupasani.linkup.databinding.FragmentCurrentUserProfileBinding
import com.vivekupasani.linkup.models.UserModel

class CurrentUserProfile : Fragment() {

    private var _binding: FragmentCurrentUserProfileBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val fireRef = FirebaseFirestore.getInstance()

    private lateinit var currentUserId: String

    private lateinit var mail: String
    private lateinit var pass: String
    private lateinit var profile: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCurrentUserProfileBinding.inflate(inflater, container, false)

        currentUserId = auth.currentUser!!.uid

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // loading current user profile
        loadUserProfile(currentUserId)
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
                    if (user != null) {
                        // Update UI with user data
                        binding.apply {
                            profileName.text = user.name
                            profileProfession.text = user.proffesion
                            profileBio.text = user.bio
                            profileFollowers.text = user.follower.toString()
                            profileFollowing.text = user.following.toString()
                            profilePost.text = user.post.toString()
                        }

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
        if (inProgress) {
            binding.progressBar.isVisible = true
            binding.actionBar.isVisible = false
        } else {
            binding.progressBar.isVisible = false
            binding.actionBar.isVisible = true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}