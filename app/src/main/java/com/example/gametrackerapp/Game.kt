package com.example.gametrackerapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val genre: String,
    val release: String,
    val platform: String,
    val status: String,
    val rating: String,
    val startDate: String,
    val finishDate: String,
    val favorite: Boolean = false
)
