package com.example.trackmate.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trackmate.R
import com.example.trackmate.db.Track
import com.example.trackmate.others.TrackingUtility
import com.example.trackmate.ui.fragments.DetailsFragment
import kotlinx.android.synthetic.main.fragment_tracks.view.*
import kotlinx.android.synthetic.main.item_track.view.*
import java.text.SimpleDateFormat
import java.util.*

class TrackAdapter : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    inner class TrackViewHolder(itemView : View)  : RecyclerView.ViewHolder(itemView)


    val diffCallback = object : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)
    fun submitList(list : List<Track>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_track,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(track.img).into(ivTrackImage)
            val formattedDate = TrackingUtility.formatDate(track.timestamp)
            tvDate.text = formattedDate
            tvCalories.text = "${ track.caloriesBurned } Kcal"
            tvAvgSpeed.text = "${ track.avgSpeedInKMH } Km/h"
            val formattedDistance = TrackingUtility.formatDistance(track.distanceInMeter)
            tvDistanceCovered.text = formattedDistance
            val formattedTime = TrackingUtility.formatTime(track.timeInMillis)
            tvDuration.text = formattedTime
            val timestamp = track.timestamp
            val modeSelected = track.mode
            if(modeSelected=="Walking"){
                ivMode.setImageResource(R.drawable.walk)
            }else if(modeSelected=="Running"){
                ivMode.setImageResource(R.drawable.running)
            }else if(modeSelected=="Cycling"){
                ivMode.setImageResource(R.drawable.cycling)
            }else{
                ivMode.setImageResource(R.drawable.running)
            }
        }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(track)
        }
    }

    var onItemClick : ((Track) -> Unit)? = null

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}