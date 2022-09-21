/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.android.unscramble.ui.game

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.MainActivity
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {


    /** NOTE 2: Property Delegation
     * Property Delegation in Kotlin helps you to handoff the getter-setter responsibility to a different class.
     * If you initialize the view model using default GameViewModel constructor, like below:
     *
     *      private val viewModel = GameViewModel()
     *
     * Then the app will lose the state of the viewModel reference when the device goes through a configuration change.
     * (For example, if you rotate the device, then the activity is destroyed and created again, and you'll have a new view model instance with the initial state again.)
     *
     * Instead, use the property delegate approach and delegate the responsibility of the viewModel object to a separate class called viewModels.
     * That means when you access the viewModel object, it is handled internally by the delegate class, viewModels.
     * The delegate class creates the viewModel object for you on the first access, and retains its value through configuration changes and returns the value when requested.
     */
    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment
    val viewModel: GameViewModel by viewModels()

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GameFragmentBinding.inflate(inflater, container, false)

        Log.d("GameFragment", "onCreateView: GameFragment created/re-created!")
        Log.d("GameFragment", "onCreateView: Word: ${viewModel.currentScrambledWord} " +
                "Score: ${viewModel.score} WordCount: ${viewModel.currentWordCount}")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup a click listener for the Submit and Skip buttons.
        binding.submitBtn.setOnClickListener { onSubmitWord() }
        binding.skipBtn.setOnClickListener { onSkipWord() }

        // Update the UI
        updateNextWordOnScreen()
        binding.score.text = getString(R.string.score, 0)
        binding.wordCount.text = getString(
                R.string.word_count, 0, MAX_NO_OF_WORDS)
    }

    // Displays the next scrambled word on screen.
    private fun updateNextWordOnScreen() {
        binding.unscrambledWordTextView.text = viewModel.currentScrambledWord
    }

    // Checks the user's word, and updates the score accordingly.
    // Displays the next scrambled word.
    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()
        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (viewModel.nextWord()) {
                updateNextWordOnScreen()
            } else {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }

    // Skips the current word without changing the score.
    // Increases the word count.
    private fun onSkipWord() {
        if (viewModel.nextWord()) {
            setErrorTextField(false)
            updateNextWordOnScreen()
        } else {
            showFinalScoreDialog()
        }
    }

    // Gets a random word for the list of words and shuffles the letters in it.
    private fun getNextScrambledWord(): String {
        val tempWord = allWordsList.random().toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }

    // Re-initializes the data in the ViewModel and updates the views with the new data,
    // to restart the game.
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
        updateNextWordOnScreen()
    }

    // Exits the game.
    private fun exitGame() {
        activity?.finish()
    }

    // Sets and resets the text field error status.
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    // Creates and shows an AlertDialog with the final score.
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score))
            .setCancelable(false)
            /* without lambda */
            .setPositiveButton(getString(R.string.play_again), DialogInterface.OnClickListener { dialog, which ->
                exitGame()
            })
            /* with lambda */
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .show()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("GameFragment", "GameFragment detached!")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("GameFragment", "GameFragment destroyed!")
    }
}
