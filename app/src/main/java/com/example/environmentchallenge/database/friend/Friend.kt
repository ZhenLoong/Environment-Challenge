package com.example.environmentchallenge.database.friend

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="friend_table")
data class Friend(
    @PrimaryKey(autoGenerate = true)
    val friendId:Long = 0L,

    @ColumnInfo(name="facebook_id")
    val fbID: String,

    @ColumnInfo(name="friend_name")
    val friendName: String,

    @ColumnInfo(name="friend_picUrl")
    val friendUrl:String
)