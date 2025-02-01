package com.example.gametrackerapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Defines the Room database for Game entities, with a version number for migrations
@Database(entities = [Game::class], version = 2) // Increment the version when making schema changes
abstract class GameDatabase : RoomDatabase() {

    // Abstract function to access GameDao, which provides methods for database operations
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null // Singleton instance to prevent multiple database instances

        // Reference: The following code was from the assistance of ChatGPT and have been modified to fit the Game Tracker app
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE games ADD COLUMN favorite INTEGER NOT NULL DEFAULT 0")
            }
        }
        // Reference complete

        // Returns the singleton instance of GameDatabase, creating it if it doesn’t exist
        fun getDatabase(context: Context): GameDatabase {
            // Checks if INSTANCE is already created; if not, it synchronizes to ensure only one instance is created
            return INSTANCE ?: synchronized(this) {
                // Builds the Room database with the specified configuration
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "game_database" //Sets name for database file
                ) .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance // Sets the INSTANCE to the newly created database
                instance // Returns the database instance
            }

        }

    }
}