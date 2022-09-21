package com.example.android.unscramble.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

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

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData<Int>(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    // // Something for accessibility, so Talkback can read the scrambled word verbatim (letter by letter).
    // val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambledWord) {
    //     if (it == null) {
    //         SpannableString("")
    //     } else {
    //         val scrambledWord = it.toString()
    //         val spannable: Spannable = SpannableString(scrambledWord)
    //         spannable.setSpan(TtsSpan.VerbatimBuilder(scrambledWord).build(), 0, scrambledWord.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    //         spannable
    //     }
    // }


    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    /**
     * Updates [currentWord] and [_currentScrambledWord] with the next word.
     * */
    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            Log.d("Unscramble", "currentWord= $currentWord")
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
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
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    /**
     * Returns true if the current word count is less than MAX_NO_OF_WORDS.
     * Updates the next word.
     */
    fun nextWord(): Boolean {
        return if (currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    /*
    * Re-initializes the game data to restart the game.
    */
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }


}