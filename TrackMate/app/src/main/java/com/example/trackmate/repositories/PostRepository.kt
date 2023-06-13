package com.example.trackmate.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.trackmate.db.api.posts.services.DisplayPostService
import com.example.trackmate.paging.PostPagingSource

class PostRepository(private val displayPostService: DisplayPostService) {

    fun getPosts() = Pager(
        config = PagingConfig(pageSize = 5, maxSize = 20),
        pagingSourceFactory = { PostPagingSource(displayPostService) }
    ).liveData
}
