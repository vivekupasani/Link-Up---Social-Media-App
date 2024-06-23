package com.vivekupasani.linkup.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.vivekupasani.linkup.databinding.EachPostDesignBinding
import com.vivekupasani.linkup.models.PostModel

class PostAdapter(
    val context: Context,
    var postList: ArrayList<PostModel>,
    val onUsernameClicked: (String) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(val binding: EachPostDesignBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EachPostDesignBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentPost = postList[position]

        holder.binding.userName.text = currentPost.name
        holder.binding.userCaption.text = currentPost.caption

        // Initially hide the post image and show the progress bar
        holder.binding.userPost.isVisible = false
        holder.binding.progressBar.isVisible = true

        // Load the profile picture
        Picasso.get()
            .load(currentPost.profileUrl)
            .noFade()
            .into(holder.binding.userProfilepic)

        // Load the post image and handle visibility
        Picasso.get()
            .load(currentPost.postUrl)
            .noFade()
            .into(holder.binding.userPost, object : Callback {
                override fun onSuccess() {
                    holder.binding.progressBar.isVisible = false
                    holder.binding.userPost.isVisible = true
                }

                override fun onError(e: Exception?) {
                    holder.binding.progressBar.isVisible = false
                    // Optionally handle error, e.g., show a placeholder
                }
            })

        holder.binding.btnLike.isVisible = true
        holder.binding.btnLikeDone.isVisible = false

        holder.binding.btnLike.setOnClickListener {
            holder.binding.btnLike.isVisible = false
            holder.binding.btnLikeDone.isVisible = true
        }

        holder.binding.btnLikeDone.setOnClickListener {
            holder.binding.btnLike.isVisible = true
            holder.binding.btnLikeDone.isVisible = false
        }

        holder.binding.userName.setOnClickListener {
            // Navigates to Profile Fragment
            if (currentPost.userId != null) {
                onUsernameClicked(currentPost.userId)
            }
        }
    }

}
