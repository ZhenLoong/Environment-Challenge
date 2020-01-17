package com.example.environmentchallenge.database.ranking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RankingDAO {

    @Insert
    fun insert(ranking: Ranking)

    @Update
    fun update(ranking: Ranking)

    @Query("SELECT * from ranking_table WHERE rankId = :key")
    fun get(key: String): Ranking?

    @Query("SELECT * from ranking_table ORDER BY RANDOM() LIMIT 1")
    fun getRandom(): Ranking?

    @Query("DELETE from ranking_table")
    fun clear()

}