package com.example.trackmate.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.trackmate.db.api.posts.services.DisplayPostService
import com.example.trackmate.db.api.posts.postsmodel.PostX

class PostPagingSource(private val displayPostService: DisplayPostService) : PagingSource<Int, PostX>() {

    override fun getRefreshKey(state: PagingState<Int, PostX>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostX> {
        try {
            val position = params.key ?: 1
            val response = displayPostService.getPosts("https://track-mate-backend-app.onrender.com/api/post/getPost", position)

            val prevKey = if (position > 1) position - 1 else null
            val nextKey = if (position < response.totalCount) position + 1 else null

            return LoadResult.Page(
                data = response.posts,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}
