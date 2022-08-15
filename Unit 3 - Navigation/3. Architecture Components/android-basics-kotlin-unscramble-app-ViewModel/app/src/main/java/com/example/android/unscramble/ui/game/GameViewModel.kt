package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    /**
     * Note 3: Backing property
     * Declare private mutable variable that can only be modified within the class it is declared.
     * Declare another public immutable field and override its getter method.
     * Return the private property's value in the getter method.
     * When count is accessed, the get() function is called and the value of _count is returned.
     *
     * Equivalent in Java would be having a private variable with a public getter
     *       private var _count = 0
     *       val count: Int
     *           get() = _count
     */

    private var _score = 0
    val score: Int
        get() = _score

    private var _currentWordCount = 0
    val currentWordCount: Int
        get() = _currentWordCount

    private lateinit var _currentScrambledWord: String
    val currentScrambledWord: String
        get() = _currentScrambledWord

    /**
     * Updates [currentWord] and [_currentScrambledWord] with the next word.
     * */
    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        // Make sure the shuffled word didn't end up the same as the non-shuffled version of it
        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }

        // If the word was used already, get a different one.
        // If not, update the value of _currentScrambledWord
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord = String(tempWord)
            ++_currentWordCount
            wordsList.add(currentWord)
        }
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    private fun increaseScore() {
        _score += SCORE_INCREASE
    }

    /**
     * Returns true if the current word count is less than MAX_NO_OF_WORDS.
     * Updates the next word.
     */
    fun nextWord(): Boolean {
        return if (currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    /*
    * Re-initializes the game data to restart the game.
    */
    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
        wordsList.clear()
        getNextWord()
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }


}