package com.example.trackmate.db.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {
    private const val BASE_URL = "https://track-mate-backend-app.onrender.com/api/post/"
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient : OkHttpClient.Builder = OkHttpClient.Builder()
        .connectTimeout(300, TimeUnit.SECONDS) // Adjust the connection timeout value as needed
        .readTimeout(300, TimeUnit.SECONDS).addInterceptor(logger)

    private val builder : Retrofit.Builder = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient.build())

    private val retrofit : Retrofit = builder.build()

    fun <T> buildService(serviceType : Class<T>) : T {
        return retrofit.create(serviceType)
    }
}