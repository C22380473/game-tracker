package com.example.gametrackerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter class for displaying a list of Game items in a RecyclerView
class GameAdapter(private var gameList: List<Game>,
                  private val onDeleteClick: (Game) -> Unit,
                  private val onEditClick: (Game) -> Unit,
                  private val onFavoriteClick: (Game) -> Unit ) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    // ViewHolder class that represents each item view in the RecyclerView
    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.text_game_title) // TextView for game title
        val genre: TextView = itemView.findViewById(R.id.text_game_genre) // TextView for game genre
        val release: TextView = itemView.findViewById(R.id.text_game_release) // TextView for game release year
        val platform: TextView = itemView.findViewById(R.id.text_game_platform) // TextView for game platform
        val status: TextView = itemView.findViewById(R.id.text_game_status) // TextView for game status
        val rating: TextView = itemView.findViewById(R.id.text_game_rating) // TextView for game rating
        val startDate: TextView = itemView.findViewById(R.id.text_game_start_date) // TextView for game start  date
        val finishDate: TextView = itemView.findViewById(R.id.text_game_finish_date) // TextView for game finish date
        val deleteButton: Button = itemView.findViewById(R.id.button_delete) // Delete button
        val editButton: Button = itemView.findViewById(R.id.button_edit) // Edit Button
        val favoriteButton: Button = itemView.findViewById(R.id.button_favorite) // Favorite button
        val gameImage: ImageView = itemView.findViewById(R.id.imageView) // ImageView for the game image
    }

    // Creates a new ViewHolder by inflating the item layout (item_game.xml)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    // Binds data to the views in each ViewHolder based on the position
    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gameList[position]
        holder.title.text = game.title // Sets game title
        holder.genre.text = game.genre // Sets game genre
        holder.release.text = game.release // Sets game release
        holder.platform.text = game.platform // Sets game platform
        holder.status.text = game.status // Sets game status
        holder.rating.text = game.rating // Sets game rating
        holder.startDate.text = game.startDate // Sets game start date
        holder.finishDate.text = game.finishDate // Sets game finish date

        // Convert both the game title and input string to lowercase for case-insensitive comparison
        val gameTitleLowerCase = game.title.trim().toLowerCase()

        // Set up the image based on the game title
        val imageResId = when (gameTitleLowerCase) {
            "the walking dead" -> R.drawable.twdg // Add actual drawable image
            "the last of us" -> R.drawable.tlou // Add actual drawable image
            "the wolf among us" -> R.drawable.twau
            "tomb raider" -> R.drawable.tombraider
            "detroit become human" -> R.drawable.detroitbecomehuman
            "gta iv" -> R.drawable.gtaiv
            "until dawn" -> R.drawable.untildawn
            "mafia" -> R.drawable.mafia
            "heavy rain" -> R.drawable.heavyrain
            "spider-man" -> R.drawable.spiderman
            "resident evil 2" -> R.drawable.re2
            "l.a noire" -> R.drawable.lanoire
            "uncharted 2" -> R.drawable.uncharted2
            "life is strange" -> R.drawable.lis
            "silent hill 2" -> R.drawable.silenthill2
            "sleeping dogs" -> R.drawable.sleepingdogs
            else -> R.drawable.default_img // Fallback image
        }
        holder.gameImage.setImageResource(imageResId)

        // Reference: The following code was from the assistance of ChatGPT and have been modified to fit the Game Tracker app
        // Set up the delete button click listener using the passed function
        holder.deleteButton.setOnClickListener {
            onDeleteClick(game) // Call the delete function passed via lambda
        }

        // Set up the edit button
        holder.editButton.setOnClickListener {
            onEditClick(game) // Invoke the callback to edit the game
        }

        // Update button text based on favorite status
        holder.favoriteButton.text = if (game.favorite) "Unfavorite" else "Favorite"
        // Handle favorite button clicks
        holder.favoriteButton.setOnClickListener {
            onFavoriteClick(game)
        }
        // Reference complete
    }

    // Method to update the list of games
    fun updateGames(games: List<Game>) {
        this.gameList = games
        this.gameList = ArrayList(games) // Sync the filtered list
        notifyDataSetChanged() // Notify RecyclerView of data changes
    }

    // Returns the total number of items in the data list
    override fun getItemCount(): Int {
        return gameList.size
    }
}
