package com.example.trackmate.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe() : Flow<Status>

    enum class Status {
        Available, Unavailable , Lost , Loosing
    }
}