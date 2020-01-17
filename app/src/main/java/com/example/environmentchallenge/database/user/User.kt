package com.example.environmentchallenge.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0L,

    @ColumnInfo(name="user_name")
    val userName: String,

    @ColumnInfo(name="user_age")
    val userAge:String,

    @ColumnInfo(name="user_picUrl")
    val userPicUrl:String
)