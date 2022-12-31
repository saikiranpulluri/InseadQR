package com.konbini.inseadqr.datapoller

import com.konbini.inseadqr.data.LoginStatusResponse
import com.konbini.inseadqr.data.Resource
import com.konbini.inseadqr.repositories.MainRepository
import com.konbini.inseadqr.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn

class DataPoller(
    private val viewModel: MainViewModel,
    private val repository: MainRepository,
    private val dispatcher: CoroutineDispatcher
) : Poller {
    @ExperimentalCoroutinesApi
    override fun poll(delay: Long, url: String): Flow<Resource<LoginStatusResponse>> {
        return channelFlow {
            while (!isClosedForSend) {
                if (viewModel.pollingState.value == "INACTIVE") {
                    close()
                    return@channelFlow
                }
                kotlinx.coroutines.delay(delay)
                send(repository.getLoginStatusForEachTable(url))
            }
        }.flowOn(dispatcher)
    }
}