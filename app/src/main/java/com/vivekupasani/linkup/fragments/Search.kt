package com.vivekupasani.linkup.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.vivekupasani.linkup.R
import com.vivekupasani.linkup.adapters.searchAdapter
import com.vivekupasani.linkup.databinding.FragmentSearchBinding
import com.vivekupasani.linkup.models.userModel


class Search : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchList: ArrayList<userModel>
    private lateinit var adapter: searchAdapter
    lateinit var firestore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchList = ArrayList()
        firestore = FirebaseFirestore.getInstance()

        showglobaluser()
    }

    private fun showglobaluser() {
        setProgressBar(true)
        firestore.collection("Users")
            .get()
            .addOnSuccessListener { result ->
                searchList.clear()
                for (document in result) {
                    val data = document.toObject(userModel::class.java)
                    searchList.add(data)
                }
                binding.rvSearch.layoutManager = LinearLayoutManager(context)
                adapter = searchAdapter(requireContext(), searchList)
                binding.rvSearch.adapter = adapter

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