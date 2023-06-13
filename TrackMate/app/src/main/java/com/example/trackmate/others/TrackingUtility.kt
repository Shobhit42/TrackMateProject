package com.example.trackmate.others

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Build
import com.example.trackmate.services.Polyline
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TrackingUtility {

    fun hasLocationPermissions(context : Context) =
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }else{
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            )
        }

    fun calculatePolyLineLength(polyline: Polyline) : Float {
        var distance = 0f
        for(i in 0..polyline.size - 2) {
            val pos1 = polyline[i]
            val pos2 = polyline[i+1]

            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }

    fun getFormattedShopWatchTime(ms : Long , includeMillis : Boolean = false) : String{
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
        if(!includeMillis){
            return "${if(hours<10) "0" else ""}$hours:" +
                    "${if(minutes<10) "0" else ""}$minutes:" +
                    "${if(seconds<10) "0" else ""}$seconds"
        }
        milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliseconds /= 10
        return "${if(hours<10) "0" else ""}$hours:" +
                "${if(minutes<10) "0" else ""}$minutes:" +
                "${if(seconds<10) "0" else ""}$seconds:" +
                "${if(milliseconds<10) "0" else ""}$milliseconds"
    }

    fun kmhToMpm(kmh: Float): Float {
        return kmh * 1000f / 60f
    }

    fun calculateCaloriesBurned(
        bodyWeightKg: Float,
        distanceMeters: Float,
        avgSpeedMpm: Float,
        activityType: String
    ): Float {
        return when(activityType) {
            "Walking" -> (0.035f * bodyWeightKg) + ((distanceMeters / 1000f) * 0.029f * avgSpeedMpm * bodyWeightKg)
            "Running" -> (0.063f * bodyWeightKg) + ((distanceMeters / 1000f) * 0.035f * avgSpeedMpm * bodyWeightKg)
            "Cycling" -> (0.049f * bodyWeightKg) + ((distanceMeters / 1000f) * 0.021f * avgSpeedMpm * bodyWeightKg)
            else -> throw IllegalArgumentException("Invalid activity type: $activityType")
        }
    }

    fun getDayOfMonthSuffix(n: Int): String {
        if (n in 11..13) {
            return "th"
        }
        when (n % 10) {
            1 -> return "st"
            2 -> return "nd"
            3 -> return "rd"
            else -> return "th"
        }
    }

    fun formatDate(millis: Long): String {
        val sdf = SimpleDateFormat("MMMM", Locale.getDefault())
        val date = Date(millis)
        val dayOfMonth = date.date
        val suffix = getDayOfMonthSuffix(dayOfMonth)
        return "${dayOfMonth}$suffix ${sdf.format(date)}"
    }

    fun formatTime(millis: Long): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        val decimalMinutes = String.format("%.1f", minutes + seconds / 60.0)
        return "$decimalMinutes Mins"
    }

    fun formatDistance(meters: Int): String {
        val kilometers = meters / 1000
        val metersRemainder = meters % 1000
        return if (metersRemainder == 0) {
            "$kilometers km"
        } else {
            String.format("%d.%03d Km", kilometers, metersRemainder)
        }
    }
}