package com.konbini.inseadqr.data

import java.util.HashMap
import javax.inject.Inject

class MainDataSource @Inject constructor(
    private val apiService: APIService
) : BaseDataSource() {
    suspend fun checkCustomerOrderByStatus(
        url: String,
        status: String,
        customerId: String,
        basic: String
    ) = getResult {
        apiService.checkCustomerOrderByStatus(url, basic, status, customerId)
    }

    suspend fun getAuthToken(
        url: String,
        map: HashMap<String, String>
    ) = getResultWithError {
        apiService.getAuthToken(url, map)
    }

    suspend fun getLoginStatusForEachTable(
        url: String
    ) = getResultWithError {
        apiService.getLoginStatusForEachTable(url)
    }

    suspend fun enableTemporaryLogin(
        url: String,
        map: HashMap<String, String>
    ) = getResultWithError {
        apiService.enableTemporaryLogin(url, map)
    }

    suspend fun updateOrderStatus(
        url: String,
        map: HashMap<String, String>,
        basic: String
    ) = getResultWithError {
        apiService.updateOrderStatus(url, map, basic)
    }
}