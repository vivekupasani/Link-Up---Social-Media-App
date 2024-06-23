package com.vivekupasani.linkup.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vivekupasani.linkup.OtherUserProfileActivity
import com.vivekupasani.linkup.adapters.SearchAdapter
import com.vivekupasani.linkup.databinding.FragmentSearchBinding
import com.vivekupasani.linkup.models.UserModel

class Search : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchList: ArrayList<UserModel>
    private lateinit var searchAdapter: SearchAdapter

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchList = ArrayList()

        showGlobalUser()

        searchAdapter = SearchAdapter(requireContext(), searchList) { userID ->
            Intent(requireActivity(), OtherUserProfileActivity::class.java).apply {
                putExtra("userId", userID)
                startActivity(this)
            }
        }

        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }

    }

    private fun showGlobalUser() {
        setProgressBar(true)
        firestore.collection("Users")
            .get()
            .addOnSuccessListener { result ->
                searchList.clear()
                for (document in result) {
                    val data = document.toObject(UserModel::class.java)
                    if(data.userId != auth.currentUser!!.uid) {
                        searchList.add(data)
                    }
                }
                searchAdapter.notifyDataSetChanged()
                setProgressBar(false)
            }
            .addOnFailureListener{
                setProgressBar(false)
            }

    }

    private fun setProgressBar(inProgress: Boolean) {
        binding.progressBar.isVisible = inProgress
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}