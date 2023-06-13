package com.example.trackmate.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.trackmate.repositories.PostRepository

class PostViewModel (private val postRepository: PostRepository) : ViewModel() {

    val posts = postRepository.getPosts().cachedIn(viewModelScope)
}
