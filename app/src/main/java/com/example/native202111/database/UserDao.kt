package com.example.native202111.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_entity")
    suspend fun getAll(): List<UserEntity>

    @Query("SELECT * FROM user_entity WHERE user_name = :userName")
    suspend fun get(userName: String): List<UserEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insert(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

    @Query("SELECT * FROM user_entity WHERE user_name = :userName")
    fun loadUserByName(userName: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM user_entity ORDER BY user_name ASC")
    fun loadAllUser(): Flow<List<UserEntity>>
}
