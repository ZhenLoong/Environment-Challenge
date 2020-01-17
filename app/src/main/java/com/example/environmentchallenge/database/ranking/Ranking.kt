package com.example.environmentchallenge.database.ranking

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ranking_table")
data class Ranking(
    @PrimaryKey
    var rankId: String,

    @ColumnInfo(name="ranking_name")
    var rankName: String,

    @ColumnInfo(name="ranking_point")
    var rankPoint: Int
)