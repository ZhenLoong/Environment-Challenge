package com.example.environmentchallenge.database.ranking

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Ranking::class], version = 1, exportSchema = true)
abstract class RankingDatabase : RoomDatabase() {

    abstract val rankingDatabaseDAO : RankingDAO

    companion object {

        @Volatile
        private var INSTANCE: RankingDatabase? = null

        fun getInstance(context: Context): RankingDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RankingDatabase::class.java,
                        "challenge_database"
                    )
                        .allowMainThreadQueries().fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}