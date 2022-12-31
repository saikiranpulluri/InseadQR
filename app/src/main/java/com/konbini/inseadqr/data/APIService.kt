package com.konbini.inseadqr.data

import retrofit2.Response
import retrofit2.http.*

interface APIService {
    @GET
    suspend fun checkCustomerOrderByStatus(
        @Url url: String,
        @Header("Authorization") h1:String,
        @Query("status") consumer_key: String?,
        @Query("customer") consumer_secret: String?
    ): Response<CustomerOrdersByStatusModel>

    @POST
    suspend fun getAuthToken(@Url url: String, @Body headers: HashMap<String, String>): Response<AuthResponse>

    @GET
    suspend fun getLoginStatusForEachTable(@Url url: String): Response<LoginStatusResponse>

    @POST
    suspend fun enableTemporaryLogin(@Url url: String, @Body headers: HashMap<String, String>): Response<EnableTemporaryLoginResponse>

    @POST
    suspend fun disableTemporaryLogin(@Url url: String, @Body headers: HashMap<String, String>): Response<EnableTemporaryLoginResponse>

    @PUT
    suspend fun updateOrderStatus(
        @Url url: String,
        @Body headers: HashMap<String, String>,
        @Header("Authorization") h1:String
    ): Response<CustomerOrdersByStatusModelItem>
}