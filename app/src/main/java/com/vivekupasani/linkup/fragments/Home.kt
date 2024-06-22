package com.vivekupasani.linkup.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.vivekupasani.linkup.R
import com.vivekupasani.linkup.adapters.postAdapter
import com.vivekupasani.linkup.databinding.FragmentHomeBinding
import com.vivekupasani.linkup.editProfile
import com.vivekupasani.linkup.models.postModel
import com.vivekupasani.linkup.onBoardOptions

class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var postList: ArrayList<postModel>
    lateinit var adapter: postAdapter

    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize your views or set up your data here
        postList = ArrayList()

        binding.homeRv.layoutManager = LinearLayoutManager(context)
        adapter = postAdapter(requireContext(), postList)
        binding.homeRv.adapter = adapter

//        binding.logedUserpf.setOnClickListener {
//            startActivity(Intent(context,editProfile::class.java))
//            activity?.finish()
//        }

        auth = FirebaseAuth.getInstance()
        binding.logOut.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                .setTitle("Link Up")
                .setMessage("Log out of your account?")
                .setPositiveButton("Log out",DialogInterface.OnClickListener { dialog, which ->
                    auth.signOut()
                    startActivity(Intent(context,onBoardOptions::class.java))
                    activity?.finish()
                })
                .setNegativeButton("Cancel",DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                .show()
        }
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("Posts")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                postList.clear()
                for (results in result) {
                    val data = results.toObject(postModel::class.java)
                    postList.add(data)
                }
                adapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
