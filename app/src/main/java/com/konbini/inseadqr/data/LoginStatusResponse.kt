package com.konbini.inseadqr.data

import com.google.gson.annotations.SerializedName

data class LoginStatusResponse(
    @SerializedName("result")
    val result: String? = null,

    @SerializedName("success")
    var success: Boolean = false,

    @SerializedName("data")
    val data: ArrayList<TableState>
)

data class TableState(
    @SerializedName("user_id")
    val userId: String? = null,

    @SerializedName("status")
    val status: String? = null
)
