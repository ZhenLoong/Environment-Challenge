package com.example.environmentchallenge.database.user

import androidx.room.*

@Dao
interface UserDAO {
    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("DELETE FROM user_table")
    fun clearAll()

    @Query("SELECT * from user_table WHERE userId = :key")
    fun get(key: Long): User?

    @Query("SELECT * FROM user_table")
    fun getAll(): List<User>
}