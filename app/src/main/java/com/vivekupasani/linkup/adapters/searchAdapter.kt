package com.vivekupasani.linkup.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vivekupasani.linkup.databinding.EachUserInSearchbarBinding
import com.vivekupasani.linkup.models.userModel

class searchAdapter(val context: Context, val searchList: ArrayList<userModel>) :
    RecyclerView.Adapter<searchAdapter.viewHolder>() {

    class viewHolder(val binding: EachUserInSearchbarBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(
            EachUserInSearchbarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentUser = searchList[position]

        holder.binding.searchName.text = currentUser.name
        Picasso
            .get()
            .load(currentUser.profilePic)
            .noFade()
            .into(holder.binding.searchProfilepic)
    }
}