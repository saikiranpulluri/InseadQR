package com.konbini.inseadqr.data

import com.google.gson.annotations.SerializedName

data class EnableTemporaryLoginResponse(
    @SerializedName("result")
    val result: String? = null,

    @SerializedName("success")
    var success: Boolean = false,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val tempResponse: TempResponse
)

data class TempResponse(
    @SerializedName("temporary_login_url")
    val temporaryLoginUrl: String? = null,
)
