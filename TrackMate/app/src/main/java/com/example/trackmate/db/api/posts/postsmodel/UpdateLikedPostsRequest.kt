package com.example.trackmate.db.api.posts.postsmodel

data class UpdateLikedPostsRequest(
    val postId: String,
    val likes: String,
)
