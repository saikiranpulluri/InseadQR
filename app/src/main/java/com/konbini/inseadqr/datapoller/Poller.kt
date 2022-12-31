package com.konbini.inseadqr.datapoller

import com.konbini.inseadqr.data.LoginStatusResponse
import com.konbini.inseadqr.data.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface Poller {
    @ExperimentalCoroutinesApi
    fun poll(delay: Long, url: String): Flow<Resource<LoginStatusResponse>>
}