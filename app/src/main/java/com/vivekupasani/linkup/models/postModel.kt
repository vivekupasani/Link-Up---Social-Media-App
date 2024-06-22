package com.vivekupasani.linkup.models


data class postModel(
    val postId: String? = null,
    val userId: String? = null,
    val name : String? = null,
    val profileUrl : String? = null,
    val caption: String? = null,
    val postUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis()
){

}
