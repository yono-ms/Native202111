package com.example.native202111.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName("repos_url")
    val reposUrl: String
)
