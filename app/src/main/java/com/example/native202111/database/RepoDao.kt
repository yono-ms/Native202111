package com.example.native202111.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDao {
    @Query("SELECT * FROM repo_entity")
    suspend fun getAll(): List<RepoEntity>

    @Query("SELECT * FROM repo_entity WHERE user_name = :userName")
    suspend fun get(userName: String): List<RepoEntity>

    @Query("SELECT * FROM repo_entity WHERE user_id = :userId")
    suspend fun get(userId: Int): List<RepoEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insert(repoEntity: RepoEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(vararg repoEntity: RepoEntity)

    @Delete
    suspend fun delete(repoEntity: RepoEntity)

    @Query("SELECT * FROM repo_entity WHERE user_name = :userName ORDER BY update_at DESC")
    fun loadRepoByUserName(userName: String): Flow<List<RepoEntity>>
}
