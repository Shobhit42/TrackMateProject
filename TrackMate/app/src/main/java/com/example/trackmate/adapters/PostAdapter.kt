package com.example.trackmate.adapters

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.trackmate.R
import com.example.trackmate.db.api.posts.postsmodel.PostX
import com.example.trackmate.db.api.ServiceBuilder
import com.example.trackmate.db.api.posts.postsmodel.UpdateLikedPostsRequest
import com.example.trackmate.db.api.createuser.services.UserService
import com.example.trackmate.others.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PostAdapter(
    var likedPosts: MutableList<String> = mutableListOf(),
    private val sharedPreferences: SharedPreferences) : PagingDataAdapter<PostX, PostAdapter.ViewHolder>(POST_COMPARATOR) {

    private var onLikeClickListener: OnLikeClickListener? = null

    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<PostX>() {
            override fun areItemsTheSame(oldItem: PostX, newItem: PostX): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: PostX, newItem: PostX): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.caloriesBurnedTextView.text = post?.caloriesBurned
        holder.speedTextView.text = post?.avgSpeed
        holder.durationTextView.text = post?.duration

        val profileNumber = post?.userProfile
        if(profileNumber=="male1"){
            holder.userProfileImageView.setImageResource(R.drawable.male1)
        }else if(profileNumber=="male2"){
            holder.userProfileImageView.setImageResource(R.drawable.male2)
        }else if(profileNumber=="male3"){
            holder.userProfileImageView.setImageResource(R.drawable.male3)
        }else if(profileNumber=="female1"){
            holder.userProfileImageView.setImageResource(R.drawable.female1)
        }else if(profileNumber=="female2"){
            holder.userProfileImageView.setImageResource(R.drawable.female2)
        }else{
            holder.userProfileImageView.setImageResource(R.drawable.female3)
        }

        val base64String = post?.postImage

        if (base64String != null) {
            val byteArray = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            holder.postImage.setImageBitmap(bitmap)
        } else {
            // Handle the case when base64String is null
            // For example, you could set a placeholder image or hide the ImageView
            holder.postImage.setImageDrawable(null)
        }


        holder.userName.text = post?.userName

        val currTimestamp = Calendar.getInstance().timeInMillis

        val millisecondsPerDay = 24 * 60 * 60 * 1000L // Number of milliseconds in a day

        val postTimestamp = post?.date?.toLongOrNull()

        if (postTimestamp != null) {
            val difference = (currTimestamp - postTimestamp) / millisecondsPerDay

            val result: String = when {
                difference == 0L -> "Posted Today"
                difference < 7L -> "Posted $difference Days ago"
                difference < 30L -> "Posted ${(difference / 7L)} Weeks ago"
                else -> "Posted ${(difference / 30L)} Months ago"
            }

            holder.datePostTextView.text = result
        } else {
            // Handle the case when postTimestamp is null
            holder.datePostTextView.text = "Unknown"
        }

        holder.likesCountTextView.text = "${post?.likes} Likes"

        var isPostLiked = likedPosts.contains(post?._id)
        if (isPostLiked) {
            holder.heartImageView.setImageResource(R.drawable.heart)
        } else {
            holder.heartImageView.setImageResource(R.drawable.unselected_heart)
        }
        var currLikes = post?.likes?.toInt()

        holder.heartImageView.setOnClickListener {

            if (likedPosts.contains(post?._id) ) {
                val updatedLikes = currLikes?.minus(1)
                currLikes = updatedLikes
                likedPosts.remove(post?._id)
                post?._id?.let { it1 -> updateServerLikedPostList(it1, updatedLikes.toString()) }
                holder.likesCountTextView.text = "$updatedLikes Likes"
                holder.heartImageView.setImageResource(R.drawable.unselected_heart)
            }else{
                val updatedLikes = currLikes?.plus(1)
                currLikes = updatedLikes
                post?._id?.let { it1 -> likedPosts.add(it1) }
                post?._id?.let { it1 -> updateServerLikedPostList(it1, updatedLikes.toString()) }
                holder.likesCountTextView.text = "$updatedLikes Likes"
                holder.heartImageView.setImageResource(R.drawable.heart)
            }
        }
    }

    private fun updateServerLikedPostList(postId: String , likes : String) {

        val userService: UserService = ServiceBuilder.buildService(UserService::class.java)
        val userId = sharedPreferences.getString(Constants.KEY_USER_ID, "")!!
        val request = UpdateLikedPostsRequest(postId,likes)
        val requestCall: Call<Void> = userService.updateLikedPosts("https://track-mate-backend-app.onrender.com/api/user/update-likes/$userId",request)

        requestCall.enqueue(object : Callback<Void> {

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {

                }else{
                    Log.i("Feed Fragment", "Failed")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("Feed Fragment", "Some Error Occurred")
            }
        })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val caloriesBurnedTextView: TextView = itemView.findViewById(R.id.calories_post)
        var speedTextView: TextView = itemView.findViewById(R.id.speed_post)
        val durationTextView: TextView = itemView.findViewById(R.id.duration_post)
        val userProfileImageView: ImageView = itemView.findViewById(R.id.userProfile_post)
        val postImage: ImageView = itemView.findViewById(R.id.postImage)
        var userName: TextView = itemView.findViewById(R.id.tvProfileName)
        val datePostTextView: TextView = itemView.findViewById(R.id.tvDateOfPost)
        var heartImageView: ImageView = itemView.findViewById(R.id.ivHeart)
        var likesCountTextView: TextView = itemView.findViewById(R.id.tvLikesCount)
    }

    fun setOnLikeClickListener(listener: OnLikeClickListener) {
        onLikeClickListener = listener
    }

    interface OnLikeClickListener {
        fun onLikeClick(position: Int)
    }

}