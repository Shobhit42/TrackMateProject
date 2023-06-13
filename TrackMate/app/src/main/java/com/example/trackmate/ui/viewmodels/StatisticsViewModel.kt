package com.example.trackmate.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackmate.db.Track
import com.example.trackmate.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    val mainRepository : MainRepository
) : ViewModel() {

    val totalTime = mainRepository.getTotalTimeInMillis()
    val totalDistance = mainRepository.getTotalDistance()
    val totalCaloriesBurned = mainRepository.getTotalCaloriesBurned()
    val totalAvgSpeed = mainRepository.getTotalAvgSpeed()
//    fun getTotalCaloriesBurnedThisWeek(weekNumber: Int): LiveData<Int> {
//        return mainRepository.getTotalCaloriesBurnedThisWeek(weekNumber)
//    }

    suspend fun getTotalCaloriesBurnedThisWeek(weekNo: Int): Int {
        val result = mainRepository.getTotalCaloriesBurnedThisWeek(weekNo)
        return result ?: 0
    }

    val tracksSortedByDate = mainRepository.getAllTracksSortedByDate()

    val getWalkingTracksSortedByDate = mainRepository.getWalkingTracksSortedByDate()
    val getRunningTracksSortedByDate = mainRepository.getRunningTracksSortedByDate()
    val getCyclingTracksSortedByDate = mainRepository.getCyclingTracksSortedByDate()

    // Statistics
    val getTotalTimeInMillisWalk = mainRepository.getTotalTimeInMillisWalk()
    val getTotalCaloriesBurnedWalk = mainRepository.getTotalCaloriesBurnedWalk()
    val getTotalDistanceWalk = mainRepository.getTotalDistanceWalk()
//    fun getTotalCaloriesBurnedWalkOnWeekNumber(weekNumber: Int): Int {
//        return mainRepository.getTotalCaloriesBurnedWalkOnWeekNumber(weekNumber)
//    }

    suspend fun getTotalCaloriesBurnedWalkOnWeekNumber(weekNo: Int): Int {
        val result = mainRepository.getTotalCaloriesBurnedWalkOnWeekNumber(weekNo)
        return result ?: 0
    }

    // Running
    val getTotalTimeInMillisRun = mainRepository.getTotalTimeInMillisRun()
    val getTotalCaloriesBurnedRun = mainRepository.getTotalCaloriesBurnedRun()
    val getTotalDistanceRun = mainRepository.getTotalDistanceRun()
    suspend fun getTotalCaloriesBurnedRunOnWeekNumber(weekNo: Int): Int {
        val result = mainRepository.getTotalCaloriesBurnedRunOnWeekNumber(weekNo)
        return result ?: 0
    }

    // Cycling
    val getTotalTimeInMillisCycle = mainRepository.getTotalTimeInMillisCycle()
    val getTotalCaloriesBurnedCycle = mainRepository.getTotalCaloriesBurnedCycle()
    val getTotalDistanceCycle = mainRepository.getTotalDistanceCycle()
    suspend fun getTotalCaloriesBurnedCycleOnWeekNumber(weekNo: Int): Int {
        val result = mainRepository.getTotalCaloriesBurnedCycleOnWeekNumber(weekNo)
        return result ?: 0
    }
}