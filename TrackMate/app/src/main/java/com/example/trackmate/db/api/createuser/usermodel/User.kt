package com.example.trackmate.db.api.createuser.usermodel

data class User(
    val name: String,
    val profileImage: String,
    val posts: List<Any>,
    val likedPosts: List<Any>,
    val _id: String,
    val __v: Int
)
