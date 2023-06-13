package com.example.trackmate.repositories

import androidx.lifecycle.MutableLiveData
import com.example.trackmate.db.Track
import com.example.trackmate.db.TrackDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepository @Inject constructor(
    val trackDao: TrackDao,
) {
    suspend fun insertTrack(track : Track) = trackDao.insertTrack(track)
    suspend fun deleteTrack(track : Track) = trackDao.deleteTrack(track)

    fun getAllTracksSortedByDate() = trackDao.getAllTracksSortedByDate()
    fun getAllTracksSortedByDistance() = trackDao.getAllTracksSortedByDistance()
    fun getAllTracksSortedByTimeInMillis() = trackDao.getAllTracksSortedByTimeInMillis()
    fun getAllTracksSortedByAvgSpeed() = trackDao.getAllTracksSortedByAvgSpeed()
    fun getAllTracksSortedByCaloriesBurned() = trackDao.getAllTracksSortedByCaloriesBurned()

    fun getWalkingTracksSortedByDate() = trackDao.getWalkingTracksSortedByDate()
    fun getRunningTracksSortedByDate() = trackDao.getRunningTracksSortedByDate()
    fun getCyclingTracksSortedByDate() = trackDao.getCyclingTracksSortedByDate()

    fun getTotalAvgSpeed() = trackDao.getTotalAvgSpeed()
    fun getTotalDistance() = trackDao.getTotalDistance()
    fun getTotalCaloriesBurned() = trackDao.getTotalCaloriesBurned()
    fun getTotalTimeInMillis() = trackDao.getTotalTimeInMillis()
    //fun getTotalCaloriesBurnedThisWeek(weekNo: Int) = trackDao.getTotalCaloriesBurnedThisWeek(weekNo)

    suspend fun getTotalCaloriesBurnedThisWeek(weekNo: Int): Int {
        return withContext(Dispatchers.IO) {
            trackDao.getTotalCaloriesBurnedThisWeek(weekNo) ?: 0
        }
    }

    // Walking
    fun getTotalTimeInMillisWalk() = trackDao.getTotalTimeInMillisWalk();
    fun getTotalCaloriesBurnedWalk() = trackDao.getTotalCaloriesBurnedWalk();
    fun getTotalDistanceWalk() = trackDao.getTotalDistanceWalk();
    //suspend fun getTotalCaloriesBurnedWalkOnWeekNumber(weekNo: Int) = trackDao.getTotalCaloriesBurnedWalkOnWeekNumber(weekNo)
    suspend fun getTotalCaloriesBurnedWalkOnWeekNumber(weekNo: Int): Int {
        return withContext(Dispatchers.IO) {
            trackDao.getTotalCaloriesBurnedWalkOnWeekNumber(weekNo) ?: 0
        }
    }

    //Running
    fun getTotalTimeInMillisRun() = trackDao.getTotalTimeInMillisRun();
    fun getTotalCaloriesBurnedRun() = trackDao.getTotalCaloriesBurnedRun();
    fun getTotalDistanceRun() = trackDao.getTotalDistanceRun();
    //fun getTotalCaloriesBurnedRunOnWeekNumber(weekNo: Int) = trackDao.getTotalCaloriesBurnedRunOnWeekNumber(weekNo)
    suspend fun getTotalCaloriesBurnedRunOnWeekNumber(weekNo: Int): Int {
        return withContext(Dispatchers.IO) {
            trackDao.getTotalCaloriesBurnedRunOnWeekNumber(weekNo) ?: 0
        }
    }

    //Cycling
    fun getTotalTimeInMillisCycle() = trackDao.getTotalTimeInMillisCycle();
    fun getTotalCaloriesBurnedCycle() = trackDao.getTotalCaloriesBurnedCycle();
    fun getTotalDistanceCycle() = trackDao.getTotalDistanceCycle();
    //fun getTotalCaloriesBurnedCycleOnWeekNumber(weekNo: Int) = trackDao.getTotalCaloriesBurnedCycleOnWeekNumber(weekNo)
    suspend fun getTotalCaloriesBurnedCycleOnWeekNumber(weekNo: Int): Int {
        return withContext(Dispatchers.IO) {
            trackDao.getTotalCaloriesBurnedCycleOnWeekNumber(weekNo) ?: 0
        }
    }

}