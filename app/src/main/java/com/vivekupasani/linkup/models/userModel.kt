package com.vivekupasani.linkup.models

data class userModel(
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val proffesion : String? = null,
    val bio: String? = null,
    val profilePic: String? = null,
    val postId: String? = null,
    val follower : MutableList<String> = mutableListOf(),
    val following : MutableList<String> = mutableListOf(),
    val post : MutableList<String> = mutableListOf()
    //list of followers following, posts
){

}