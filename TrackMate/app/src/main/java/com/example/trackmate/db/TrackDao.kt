package com.example.trackmate.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)  // the old run will be replace by the new one if there is a conflict
    suspend fun insertTrack(track : Track)

    @Delete
    suspend fun deleteTrack(track : Track)

    @Query("SELECT * FROM tracking_table ORDER BY timestamp DESC")
    fun getAllTracksSortedByDate() : LiveData<List<Track>>    // live data does not work on a coroutine

    @Query("SELECT * FROM tracking_table ORDER BY timeInMillis DESC")
    fun getAllTracksSortedByTimeInMillis() : LiveData<List<Track>>

    @Query("SELECT * FROM tracking_table ORDER BY caloriesBurned DESC")
    fun getAllTracksSortedByCaloriesBurned() : LiveData<List<Track>>

    @Query("SELECT * FROM tracking_table ORDER BY avgSpeedInKMH DESC")
    fun getAllTracksSortedByAvgSpeed() : LiveData<List<Track>>

    @Query("SELECT * FROM tracking_table ORDER BY distanceInMeter DESC")
    fun getAllTracksSortedByDistance() : LiveData<List<Track>>

    @Query("SELECT SUM(timeInMillis) FROM tracking_table")   // SUM and AVG IS THE FUNCTION OF SQLite
    fun getTotalTimeInMillis() : LiveData<Long>

    @Query("SELECT SUM(caloriesBurned) FROM tracking_table")
    fun getTotalCaloriesBurned() : LiveData<Int>

    @Query("SELECT SUM(distanceInMeter) FROM tracking_table")
    fun getTotalDistance() : LiveData<Int>

    @Query("SELECT AVG(avgSpeedInKMH) FROM tracking_table")
    fun getTotalAvgSpeed() : LiveData<Float>

    @Query("SELECT SUM(caloriesBurned) FROM tracking_table WHERE weekNumber = :weekNo")
    suspend fun getTotalCaloriesBurnedThisWeek(weekNo: Int): Int?

    // Walking Statistics
    @Query("SELECT SUM(timeInMillis) FROM tracking_table WHERE mode = 'Walking'")
    fun getTotalTimeInMillisWalk() : LiveData<Long>

    @Query("SELECT SUM(caloriesBurned) FROM tracking_table WHERE mode = 'Walking'")
    fun getTotalCaloriesBurnedWalk() : LiveData<Int>

    @Query("SELECT SUM(distanceInMeter) FROM tracking_table WHERE mode = 'Walking'")
    fun getTotalDistanceWalk() : LiveData<Int>

    @Query("SELECT * FROM tracking_table WHERE mode = 'Walking' ORDER BY timestamp DESC")
    fun getWalkingTracksSortedByDate() : LiveData<List<Track>>

    @Query("SELECT SUM(caloriesBurned) FROM tracking_table WHERE weekNumber = :weekNo AND mode = 'Walking'")
    suspend fun getTotalCaloriesBurnedWalkOnWeekNumber(weekNo: Int): Int?

    // Running Statistics
    @Query("SELECT SUM(timeInMillis) FROM tracking_table WHERE mode = 'Running'")
    fun getTotalTimeInMillisRun() : LiveData<Long>

    @Query("SELECT SUM(caloriesBurned) FROM tracking_table WHERE mode = 'Running'")
    fun getTotalCaloriesBurnedRun() : LiveData<Int>

    @Query("SELECT SUM(distanceInMeter) FROM tracking_table WHERE mode = 'Running'")
    fun getTotalDistanceRun() : LiveData<Int>

    @Query("SELECT * FROM tracking_table WHERE mode = 'Running' ORDER BY timestamp DESC")
    fun getRunningTracksSortedByDate() : LiveData<List<Track>>

    @Query("SELECT SUM(caloriesBurned) FROM tracking_table WHERE weekNumber = :weekNo AND mode = 'Running'")
    fun getTotalCaloriesBurnedRunOnWeekNumber(weekNo: Int): Int?

    // Cycling Statistics
    @Query("SELECT SUM(timeInMillis) FROM tracking_table WHERE mode = 'Cycling'")
    fun getTotalTimeInMillisCycle() : LiveData<Long>

    @Query("SELECT SUM(caloriesBurned) FROM tracking_table WHERE mode = 'Cycling'")
    fun getTotalCaloriesBurnedCycle() : LiveData<Int>

    @Query("SELECT SUM(distanceInMeter) FROM tracking_table WHERE mode = 'Cycling'")
    fun getTotalDistanceCycle() : LiveData<Int>

    @Query("SELECT * FROM tracking_table WHERE mode = 'Cycling' ORDER BY timestamp DESC")
    fun getCyclingTracksSortedByDate() : LiveData<List<Track>>

    @Query("SELECT SUM(caloriesBurned) FROM tracking_table WHERE weekNumber = :weekNo AND mode = 'Cycling'")
    fun getTotalCaloriesBurnedCycleOnWeekNumber(weekNo: Int): Int?
}