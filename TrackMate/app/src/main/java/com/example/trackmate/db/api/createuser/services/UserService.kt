package com.example.trackmate.db.api.createuser.services

import com.example.trackmate.db.api.posts.postsmodel.UpdateLikedPostsRequest
import com.example.trackmate.db.api.createuser.usermodel.CreateUserRequest
import com.example.trackmate.db.api.createuser.usermodel.CreateUserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @POST
    fun createUser(@Url url: String, @Body newUser: CreateUserRequest): Call<CreateUserResponse>

    @GET
    fun getUser(@Url url: String): Call<CreateUserResponse>

    @POST
    fun updateLikedPosts(@Url url: String, @Body request: UpdateLikedPostsRequest): Call<Void>

}