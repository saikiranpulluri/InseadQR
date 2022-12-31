package com.konbini.inseadqr.data

import com.google.gson.annotations.SerializedName

data class AuthResponse(

    @SerializedName("access_token")
    val accessToken: String? = null,

    @SerializedName("expires_in")
    val expiresIn: Int? = null,

    @SerializedName("token_type")
    val tokenType: String? = null,

    @SerializedName("scope")
    val scope: String? = null,
)
