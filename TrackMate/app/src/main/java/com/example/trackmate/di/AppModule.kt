package com.example.trackmate.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.trackmate.db.TrackingDatabase
import com.example.trackmate.others.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.trackmate.others.Constants.KEY_FIRST_TIME_TOGGLE_AVATAR
import com.example.trackmate.others.Constants.KEY_GENDER
import com.example.trackmate.others.Constants.KEY_IMG
import com.example.trackmate.others.Constants.KEY_MODE
import com.example.trackmate.others.Constants.KEY_NAME
import com.example.trackmate.others.Constants.KEY_USER_ID
import com.example.trackmate.others.Constants.KEY_WEIGHT
import com.example.trackmate.others.Constants.RUNNING_DATABASE_NAME
import com.example.trackmate.others.Constants.SHARED_PREFRENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton   // so that means if we have two places in our app in which we want to inject the database then
    // each place will get its own instance but that is of course not what we want here because we want to have an application
    // write singleton instance of that database and the solution to that problem is providing a Singleton Scope
    fun provideTrackingDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        TrackingDatabase::class.java,
        RUNNING_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRunDao(db: TrackingDatabase) = db.getTrackDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext app: Context
    ) = app.getSharedPreferences(SHARED_PREFRENCES_NAME , MODE_PRIVATE)

    @Singleton
    @Provides
    @Named("Name")
    fun provideName(sharedPref: SharedPreferences) = sharedPref.getString(KEY_NAME, "") ?: ""

    @Singleton
    @Provides
    fun provideWeight(sharedPref: SharedPreferences) = sharedPref.getInt(KEY_WEIGHT, 60)

    @Singleton
    @Provides
    @Named("Gender")
    fun provideGender(sharedPref: SharedPreferences) = sharedPref.getString(KEY_GENDER, "Male") ?: "Male"

    @Singleton
    @Provides
    @Named("First")
    fun provideFirstTimeToggle(sharedPref: SharedPreferences) = sharedPref.getBoolean(
        KEY_FIRST_TIME_TOGGLE, true)

    @Singleton
    @Provides
    @Named("Second")
    fun provideFirstTimeToggleAvatar(sharedPref: SharedPreferences) = sharedPref.getBoolean(
        KEY_FIRST_TIME_TOGGLE_AVATAR, true)

    @Singleton
    @Provides
    @Named("Image")
    fun provideImg(sharedPref: SharedPreferences) = sharedPref.getString(
        KEY_IMG, "") ?: ""

    @Singleton
    @Provides
    @Named("Mode")
    fun provideMode(sharedPref: SharedPreferences) = sharedPref.getString(
        KEY_MODE, "Walk") ?: "Walk"

    @Singleton
    @Provides
    @Named("UserId")
    fun provideUserId(sharedPref: SharedPreferences) = sharedPref.getString(
        KEY_USER_ID, "") ?: ""
}