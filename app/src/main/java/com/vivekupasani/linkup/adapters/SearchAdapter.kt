package com.vivekupasani.linkup.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vivekupasani.linkup.databinding.EachUserInSearchbarBinding
import com.vivekupasani.linkup.models.UserModel

class SearchAdapter(
    val context: Context,
    private val searchList: ArrayList<UserModel>,
    private val onUserClick: (String) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    class SearchViewHolder(val binding: EachUserInSearchbarBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EachUserInSearchbarBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentUser = searchList[position]

        holder.binding.searchName.text = currentUser.name

        Picasso.get()
            .load(currentUser.profilePic)
            .noFade()
            .into(holder.binding.searchProfilepic)

        holder.binding.userCard.setOnClickListener {
            if (currentUser.userId != null) {
                onUserClick(currentUser.userId)
            }
        }
    }
}