package com.example.native202111.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoModel(
    @SerialName("name")
    val name: String,
    @SerialName("updated_at")
    val updatedAt: String
)
