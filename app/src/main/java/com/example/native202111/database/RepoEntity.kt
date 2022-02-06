package com.example.native202111.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repo_entity")
data class RepoEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "update_at") val updateAt: Long,
    @ColumnInfo(name = "update_at_text") val updateAtText: String,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "user_name") val userName: String,
)
