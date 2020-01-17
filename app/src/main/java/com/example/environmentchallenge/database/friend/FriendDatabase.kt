package com.example.environmentchallenge.database.friend

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Friend::class], version = 1, exportSchema = false)
abstract class FriendDatabase : RoomDatabase()
{
    abstract val friendDatabaseDAO : FriendDAO

    companion object {

        @Volatile
        private var INSTANCE: FriendDatabase? = null

        fun getInstance(context: Context): FriendDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FriendDatabase::class.java,
                        "friend_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}