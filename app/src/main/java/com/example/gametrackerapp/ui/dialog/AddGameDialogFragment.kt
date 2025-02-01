package com.example.gametrackerapp.ui.dialog

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.gametrackerapp.Game
import com.example.gametrackerapp.R

// Reference: The following code is from AndroidStudio's built-in Bottom Navigation Views Activity Template

// Add Game Dialog Fragment displaying a dialog for user to add their games in a RecyclerView
class AddGameDialogFragment : DialogFragment() {

    private lateinit var titleEditText: EditText // EditText for game title
    private lateinit var genreEditText: EditText // EditText for game genre
    private lateinit var releaseEditText: EditText // EditText for game release year
    private lateinit var platformEditText: EditText // EditText for game platform
    private lateinit var statusEditText: EditText // EditText for game status
    private lateinit var ratingEditText: EditText // EditText for game rating
    private lateinit var startDateEditText: EditText // EditText for game start date
    private lateinit var finishDateEditText: EditText // EditText for game finish date
    private lateinit var saveButton: Button // save button
    private lateinit var cancelButton: Button // cancel button


    var onGameAdded: ((Game) -> Unit)? = null  // Callback for when a game is added

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this dialog
        val view = inflater.inflate(R.layout.fragment_add_game_dialog, container, false)

        // Initialize EditTexts and Buttons
        titleEditText = view.findViewById(R.id.edit_game_title)
        genreEditText = view.findViewById(R.id.edit_game_genre)
        releaseEditText = view.findViewById(R.id.edit_game_release)
        platformEditText = view.findViewById(R.id.edit_game_platform)
        statusEditText = view.findViewById(R.id.edit_game_status)
        ratingEditText = view.findViewById(R.id.edit_game_rating)
        startDateEditText = view.findViewById(R.id.edit_game_start_date)
        finishDateEditText = view.findViewById(R.id.edit_game_finish_date)

        saveButton = view.findViewById(R.id.button_save)
        cancelButton = view.findViewById(R.id.button_cancel)

        // Set up save button click listener
        saveButton.setOnClickListener {
            // Validate and create a new game object
            val newGame = Game(
                title = titleEditText.text.toString(),
                genre = genreEditText.text.toString(),
                release = releaseEditText.text.toString(),
                platform = platformEditText.text.toString(),
                status = statusEditText.text.toString(),
                rating = ratingEditText.text.toString(),
                startDate = startDateEditText.text.toString(),
                finishDate = finishDateEditText.text.toString()
            )

            // Reference: The following code was from the aid of ChatGPT and have been modified accordingly to fit the Game Tracker app
            // Pass the new game back to the calling fragment
            onGameAdded?.invoke(newGame)
            dismiss()  // Close the dialog
        }

        // Set up cancel button click listener
        cancelButton.setOnClickListener {
            dismiss()  // Close the dialog without saving
        }

        // DatePickerDialog setup for start date and finish date
        val calendar = Calendar.getInstance()

        val startDateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            // Format the date and set it to the start date EditText
            val formattedDate = "$dayOfMonth/${month + 1}/$year"
            startDateEditText.setText(formattedDate)
        }

        val finishDateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            // Format the date and set it to the finish date EditText
            val formattedDate = "$dayOfMonth/${month + 1}/$year"
            finishDateEditText.setText(formattedDate)
        }

        // Set OnClickListener for the start date EditText
        startDateEditText.setOnClickListener {
            DatePickerDialog(requireContext(), startDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Set OnClickListener for the finish date EditText
        finishDateEditText.setOnClickListener {
            DatePickerDialog(requireContext(), finishDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        return view
    }
    // Reference complete
}
// Reference complete
