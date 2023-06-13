package com.example.trackmate.db.api.posts.services

import com.example.trackmate.db.api.posts.postsmodel.Post
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface DisplayPostService {

    @GET
    suspend fun getPosts(@Url url: String, @Query("page") page : Int) : Post
}
