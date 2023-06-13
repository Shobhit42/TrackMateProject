package com.example.trackmate.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackmate.R
import com.example.trackmate.adapters.LoaderAdapter
import com.example.trackmate.adapters.PostAdapter
import com.example.trackmate.db.api.posts.services.DisplayPostService
import com.example.trackmate.db.api.posts.postsmodel.PostX
import com.example.trackmate.db.api.ServiceBuilder
import com.example.trackmate.db.api.createuser.services.UserService
import com.example.trackmate.db.api.createuser.usermodel.CreateUserResponse
import com.example.trackmate.others.Constants
import com.example.trackmate.repositories.PostRepository
import com.example.trackmate.ui.viewmodels.PostViewModel
import com.example.trackmate.ui.viewmodels.PostViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_feed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : Fragment(R.layout.fragment_feed) , PostAdapter.OnLikeClickListener  {

    private lateinit var postAdapter: PostAdapter
    val TAG = "FeedFragment"
    private lateinit var postList : List<PostX>
    private var likedPost : MutableList<String> = mutableListOf()

    private lateinit var viewModel: PostViewModel
    //private lateinit var adapter: PostAdapter
    @Inject
    lateinit var sharedPref : SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserLikedPosts()
        //loadPosts()


        val displayPostService: DisplayPostService = ServiceBuilder.buildService(DisplayPostService::class.java)

        val postRepository = PostRepository(displayPostService) // Initialize your PostRepository

        val viewModelFactory = PostViewModelFactory(postRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostViewModel::class.java)
        // Initialize the postAdapter
        postAdapter = PostAdapter(likedPost, sharedPref)
        postAdapter.setOnLikeClickListener(this@FeedFragment)

        // Set up the RecyclerView
        rvPosts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter.withLoadStateHeaderAndFooter(
                header = LoaderAdapter(),
                footer = LoaderAdapter()
            )
        }

        // Observe the posts LiveData in the ViewModel
        viewModel.posts.observe(viewLifecycleOwner, Observer {
            postAdapter.submitData(lifecycle, it)
        })

        // Load user's liked posts
        loadUserLikedPosts()
    }

    private fun loadUserLikedPosts() {
        val userService: UserService = ServiceBuilder.buildService(UserService::class.java)
        val userId = sharedPref.getString(Constants.KEY_USER_ID, "")!!
        val requestCall: Call<CreateUserResponse> = userService.getUser("https://track-mate-backend-app.onrender.com/api/user/$userId")

        requestCall.enqueue(object : Callback<CreateUserResponse> {

            override fun onResponse(call: Call<CreateUserResponse>, response: Response<CreateUserResponse>) {
                if (response.isSuccessful) {
                    val likedPosts = response.body()?.user?.likedPosts as? List<String>
                    if (likedPosts != null) {
                        postAdapter.likedPosts = likedPosts.toMutableList()
                    } else {
                        // Handle the case where likedPosts is null
                        // You can assign an empty list or handle it based on your requirements
                        postAdapter.likedPosts = mutableListOf()
                    }
                }else{
                    Log.i("Feed Fragment", "Failed")
                }
            }

            override fun onFailure(call: Call<CreateUserResponse>, t: Throwable) {
                Log.i("Feed Fragment", "Some Error Occurred")
            }
        })
    }

//    private fun loadPosts() {
//        val displayPostService: DisplayPostService = ServiceBuilder.buildService(DisplayPostService::class.java)
//        val requestCall: Call<Post> = displayPostService.getPosts("https://track-mate-backend-app.onrender.com/api/post")
//
//        Log.d("MainActivity", "$requestCall")
//
//        requestCall.enqueue(object : Callback<Post> {
//
//            override fun onResponse(call: Call<Post>, response: Response<Post>) {
//                if (response.isSuccessful) {
//                    val postResponse = response.body()
//                    if (postResponse != null) {
//                        postList = postResponse.posts
//                        postAdapter = PostAdapter(postList,likedPost,sharedPref)
//                        postAdapter.setOnLikeClickListener(this@FeedFragment)
//                        rvPosts.apply {
//                            layoutManager = LinearLayoutManager(requireContext())
//                            adapter = postAdapter
//                        }
//                        response.body()?.let {
//                            for (post in it.posts) {
//                                Log.d(TAG, post.toString())
//                            }
//                        }
//                    }else{
//                        Toast.makeText(requireContext() , "There is No post to show" , Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<Post>, t: Throwable) {
//                Log.i("MainActivity", "Failed")
//            }
//        })
 //   }

    override fun onLikeClick(position: Int) {

    }

}