package com.example.gametrackerapp.ui.wishlist

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gametrackerapp.Game
import com.example.gametrackerapp.GameAdapter
import com.example.gametrackerapp.GameDatabase
import com.example.gametrackerapp.R
import com.example.gametrackerapp.databinding.FragmentWishlistBinding
import kotlinx.coroutines.launch

// Reference: The following code is from AndroidStudio's built-in Bottom Navigation Views Activity Template

// Wishlist Fragment displaying a wishlist of all of users games in a RecyclerView
class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var filteredGames: List<Game> = emptyList()  // Holds the filtered games list
    private lateinit var gameRecyclerView: RecyclerView // RecyclerView for displaying game items
    private lateinit var gameAdapter: GameAdapter // Adapter to bind game data to RecyclerView
    private lateinit var gameDatabase: GameDatabase // Room database instance for storing and retrieving games
    private lateinit var searchEditText: EditText // Reference for search EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize RecyclerView and its layout manager
        gameRecyclerView = binding.recyclerViewGames
        gameRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the Room database instance
        gameDatabase = GameDatabase.getDatabase(requireContext())

        // Reference: The following code was from the assistance of ChatGPT and have been modified accordingly to fit the Game Tracker app
        // Initialize the adapter with an empty list and set it to the RecyclerView
        gameAdapter = GameAdapter(
            emptyList(),
            { game -> deleteGameFromDatabase(game)},
            { game -> showEditGameDialog(game)},
            { game -> toggleFavoriteStatus(game)}
        )
        gameRecyclerView.adapter = gameAdapter

        // Initialize the search EditText
        searchEditText = binding.searchEditText

        // Set up the TextWatcher for the search EditText
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // When text changes, call the search function to filter the games
                val query = s.toString()
                searchGames(query)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        // Reference complete
        readGamesFromDatabase() // Retrieve games and display them

        return root
    }

    // Reads all games in wishlist from the database and updates the RecyclerView
    private fun readGamesFromDatabase() {
        // Launches a coroutine to handle database operations on a background thread
        lifecycleScope.launch {
            // Fetch only games with status "Wishlist"
            val gamesWishlist = gameDatabase.gameDao().getGamesByStatus("Wishlist")
            filteredGames = gamesWishlist  // Store the filtered games here
            // Calls the adapter’s update method to refresh data
            updateRecyclerView(gamesWishlist)
        }
    }

    // Delete game from the database
    private fun deleteGameFromDatabase(game: Game) {
        lifecycleScope.launch {
            gameDatabase.gameDao().deleteGame(game) // Retrieve game entry
            Log.d("Game", "Deleted: ${game.title}") // Log deleted game for debugging
            readGamesFromDatabase() // Refresh the game list
        }
    }

    // Update game list in database
    private fun updateGameInDatabase(game: Game) {
        lifecycleScope.launch {
            gameDatabase.gameDao().updateGame(game) // Update game entry
            Log.d("Game", "Updated: ${game.title}") // Log updated game for debugging
            readGamesFromDatabase() // Refresh the game list
        }
    }

    // Updates the RecyclerView with a new list of games
    private fun updateRecyclerView(games: List<Game>) {
        gameAdapter.updateGames(games) // Calls the adapter’s update method to refresh data
    }

    // The following code was from the assistance of ChatGPT and have been modified accordingly to fit the Game Tracker app
    // Search bar function
    private fun searchGames(query: String) {
        // Filter the list within the current filtered list, even if it's empty
        val filteredList = filteredGames.filter { game ->
            game.title.contains(query, ignoreCase = true) ||
                    game.genre.contains(query, ignoreCase = true) ||
                    game.platform.contains(query, ignoreCase = true)
        }
        // Update the RecyclerView with the filtered list, even if it's empty
        updateRecyclerView(filteredList)
    }

    // Add favorite/Remove favorite
    private fun toggleFavoriteStatus(game: Game) {
        lifecycleScope.launch {
            val updatedGame = game.copy(favorite = !game.favorite) // Toggle the favorite status
            gameDatabase.gameDao().updateGame(updatedGame) // Update in database
            readGamesFromDatabase() // Reload the game list
        }
    }

    // Show Edit Game Dialog
    private fun showEditGameDialog(game: Game) {
        // Inflate the dialog layout
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_game, null)

        // Get references to the EditTexts from the inflated layout
        val editTitle = dialogView.findViewById<EditText>(R.id.edit_game_title)
        val editGenre = dialogView.findViewById<EditText>(R.id.edit_game_genre)
        val editReleaseYear = dialogView.findViewById<EditText>(R.id.edit_game_release)
        val editPlatform = dialogView.findViewById<EditText>(R.id.edit_game_platform)
        val editStatus = dialogView.findViewById<EditText>(R.id.edit_game_status)
        val editRating = dialogView.findViewById<EditText>(R.id.edit_game_rating)
        val editStartDate = dialogView.findViewById<EditText>(R.id.edit_game_start_date)
        val editFinishDate = dialogView.findViewById<EditText>(R.id.edit_game_finish_date)

        // Pre-fill fields with the game's current data
        editTitle.setText(game.title)
        editGenre.setText(game.genre)
        editReleaseYear.setText(game.release)
        editPlatform.setText(game.platform)
        editStatus.setText(game.status)
        editRating.setText(game.rating)
        editStartDate.setText(game.startDate)
        editFinishDate.setText(game.finishDate)

        // Set up date picker for start date and finish date
        val calendar = Calendar.getInstance()

        // DatePickerDialog listener for start date
        val startDateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            // Format the date and set it to the editStartDate field
            val formattedDate = "$dayOfMonth/${month + 1}/$year"
            editStartDate.setText(formattedDate)
        }

        // DatePickerDialog listener for finish date
        val finishDateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            // Format the date and set it to the editFinishDate field
            val formattedDate = "$dayOfMonth/${month + 1}/$year"
            editFinishDate.setText(formattedDate)
        }

        // Set OnClickListener for the start date EditText to show the DatePickerDialog
        editStartDate.setOnClickListener {
            DatePickerDialog(requireContext(), startDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Set OnClickListener for the finish date EditText to show the DatePickerDialog
        editFinishDate.setOnClickListener {
            DatePickerDialog(requireContext(), finishDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Create the AlertDialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Game")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                // Collect updated data from the EditText fields
                val updatedGame = game.copy(
                    title = editTitle.text.toString(),
                    genre = editGenre.text.toString(),
                    release = editReleaseYear.text.toString(),
                    platform = editPlatform.text.toString(),
                    status = editStatus.text.toString(),
                    rating = editRating.text.toString(),
                    startDate = editStartDate.text.toString(),
                    finishDate = editFinishDate.text.toString()
                )
                // Update the game in the database
                updateGameInDatabase(updatedGame)
            }
            .setNegativeButton("Cancel", null) // Close dialog without saving
            .create()

        dialog.show()
    }
    // Reference complete

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
// Reference complete
