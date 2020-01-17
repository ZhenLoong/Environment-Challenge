package com.example.environmentchallenge.database.friend

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FriendDAO {
    @Insert
    fun insert(friend: Friend)

    @Update
    fun update(friend: Friend)

    @Query("DELETE FROM friend_table")
    fun clearAll()

    @Query("SELECT * from friend_table WHERE friendId = :key")
    fun get(key: Long): Friend?

    @Query("SELECT * FROM friend_table")
    fun getAll(): List<Friend>
}