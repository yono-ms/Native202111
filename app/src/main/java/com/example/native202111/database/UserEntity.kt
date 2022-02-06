package com.example.native202111.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_entity")
data class UserEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "update_at") val updateAt: Long,
    @ColumnInfo(name = "update_at_text") val updateAtText: String,
    @ColumnInfo(name = "repos_url") val reposUrl: String,
)
