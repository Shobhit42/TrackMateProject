package com.example.trackmate.ui.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.work.*
import com.example.trackmate.db.Track
import com.example.trackmate.others.Constants
import com.example.trackmate.others.SortType
import com.example.trackmate.repositories.MainRepository
import com.example.trackmate.worker.PostsCheckWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository : MainRepository
) : ViewModel() {

    private val tracksSortedByDate = mainRepository.getAllTracksSortedByDate()
    private val tracksSortedByDistance = mainRepository.getAllTracksSortedByDistance()
    private val tracksSortedByCaloriesBurned = mainRepository.getAllTracksSortedByCaloriesBurned()
    private val tracksSortedByTimeInMillis = mainRepository.getAllTracksSortedByTimeInMillis()
    private val tracksSortedByAvgSpeed = mainRepository.getAllTracksSortedByAvgSpeed()

    private val _data = MutableLiveData<Int>()
    val data: LiveData<Int> = _data
    val tracks = MediatorLiveData<List<Track>>()
    var sortType = SortType.DATE

    fun setData(data: Int) {
        _data.value = data
    }

    init {
        tracks.addSource(tracksSortedByDate){ result ->
            if(sortType == SortType.DATE ){
                result?.let { tracks.value = it }
            }
        }
        tracks.addSource(tracksSortedByDistance){ result ->
            if(sortType == SortType.AVG_SPEED ){
                result?.let { tracks.value = it }
            }
        }
        tracks.addSource(tracksSortedByCaloriesBurned){ result ->
            if(sortType == SortType.CALORIES_BURNED ){
                result?.let { tracks.value = it }
            }
        }
        tracks.addSource(tracksSortedByTimeInMillis){ result ->
            if(sortType == SortType.DISTANCE ){
                result?.let { tracks.value = it }
            }
        }
        tracks.addSource(tracksSortedByAvgSpeed){ result ->
            if(sortType == SortType.TRACKING_TIME ){
                result?.let { tracks.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortType) = when(sortType) {
        SortType.DATE -> tracksSortedByDate.value?.let {
            tracks.value = it
        }
        SortType.TRACKING_TIME -> tracksSortedByTimeInMillis.value?.let {
            tracks.value = it
        }
        SortType.AVG_SPEED -> tracksSortedByAvgSpeed.value?.let {
            tracks.value = it
        }
        SortType.DISTANCE -> tracksSortedByDistance.value?.let {
            tracks.value = it
        }
        SortType.CALORIES_BURNED -> tracksSortedByCaloriesBurned.value?.let {
            tracks.value = it
        }
    }.also {
        this.sortType = sortType
    }

    fun insertTrack(track : Track) = viewModelScope.launch {
        mainRepository.insertTrack(track)
    }

}