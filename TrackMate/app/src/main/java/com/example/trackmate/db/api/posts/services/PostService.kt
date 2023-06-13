package com.example.trackmate.db.api.posts.services

import com.example.trackmate.db.api.posts.postsmodel.CreateNewPost
import com.example.trackmate.db.api.posts.postsmodel.Post
import retrofit2.Call
import retrofit2.http.*

interface PostService {

//    @GET("post")
//    fun getPosts() : Call<Post>

    @POST("add")
    fun createPost(@Body newUser: CreateNewPost): Call<Post>

}