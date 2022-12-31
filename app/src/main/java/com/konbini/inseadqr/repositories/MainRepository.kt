package com.konbini.inseadqr.repositories

import com.konbini.inseadqr.data.MainDataSource
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val dataSource: MainDataSource
) {
    suspend fun checkCustomerOrderByStatus(
        url: String,
        status: String,
        customerId: String,
        basic: String
    ) = dataSource.checkCustomerOrderByStatus(url, status, customerId, basic)

    suspend fun getAuthToken(
        url: String,
        map: HashMap<String, String>
    ) = dataSource.getAuthToken(url, map)

    suspend fun enableTemporaryLogin(
        url: String,
        map: HashMap<String, String>
    ) = dataSource.enableTemporaryLogin(url, map)

    suspend fun updateOrderStatus(
        url: String,
        map: HashMap<String, String>,
        basic: String
    ) = dataSource.updateOrderStatus(url, map, basic)

    suspend fun getLoginStatusForEachTable(
        url: String
    ) = dataSource.getLoginStatusForEachTable(url)
}