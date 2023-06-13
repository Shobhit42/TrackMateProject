package com.example.trackmate.db.api.posts.postsmodel

data class Post(
    val posts: List<PostX>,
    val limit: Int,
    val skip: Int,
    val totalCount: Int
)