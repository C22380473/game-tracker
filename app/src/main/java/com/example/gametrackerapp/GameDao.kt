package com.example.gametrackerapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao // Annotation indicating this is a Data Access Object (DAO)
interface GameDao {
    // Inserts a Game object into the database, replacing it if a conflict occurs
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: Game)

    // Queries the database to retrieve all Game objects
    @Query("SELECT * FROM games")
    suspend fun getAllGames(): List<Game>

    // Queries the database to retrieve all Game objects based on status
    @Query("SELECT * FROM games WHERE status = :status")
    suspend fun getGamesByStatus(status: String): List<Game>

    // Method to return all games marked as favorites
    @Query("SELECT * FROM games WHERE favorite = 1")
    suspend fun getFavoriteGames(): List<Game>

    // Query for search filtering
    @Query("SELECT * FROM games WHERE title LIKE '%' || :query || '%' OR genre LIKE '%' || :query || '%' OR platform LIKE '%' || :query || '%'")
    suspend fun searchGames(query: String): List<Game>

    // Method to update a single game by its ID
    @Update
    suspend fun updateGame(game: Game)

    // Method to delete a single game by its ID
    @Delete
    suspend fun deleteGame(game: Game)

    // Method to delete multiple games by their IDs or any condition
    @Query("DELETE FROM games WHERE id IN (:gameIds)")
    suspend fun deleteGamesByIds(gameIds: List<Long>)

    // Deletes all Game entries from the database
    @Query("DELETE FROM games")
    suspend fun deleteAllGames() // Add this method

}
