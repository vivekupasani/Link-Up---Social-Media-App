package com.vivekupasani.linkup.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vivekupasani.linkup.OtherUserProfileActivity
import com.vivekupasani.linkup.adapters.PostAdapter
import com.vivekupasani.linkup.databinding.FragmentHomeBinding
import com.vivekupasani.linkup.models.PostModel
import com.vivekupasani.linkup.OnBoardOptions

class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var postList: ArrayList<PostModel>
    private lateinit var postAdapter: PostAdapter

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize your views or set up your data here
        postList = ArrayList()

        postAdapter = PostAdapter(requireContext(), postList) { userId ->
            // Navigate to Profile Activity
            val intent = Intent(requireActivity(), OtherUserProfileActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

//        binding.logedUserpf.setOnClickListener {
//            startActivity(Intent(context,editProfile::class.java))
//            activity?.finish()
//        }

        binding.logOut.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Link Up")
                setMessage("Log out of your account?")
                setPositiveButton("Log out") { dialog, _ ->
                    auth.signOut()
                    startActivity(Intent(context,OnBoardOptions::class.java))
                    activity?.finish()
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
        }

        loadPosts()

        binding.homeRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }
    }

    private fun loadPosts() {
        firestore.collection("Posts")
            .orderBy(
                "timestamp",
                com.google.firebase.firestore.Query.Direction.DESCENDING
            )
            .get()
            .addOnSuccessListener { result ->
                postList.clear()
                for (results in result) {
                    val data = results.toObject(PostModel::class.java)
                    postList.add(data)
                }
                postAdapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
