package com.example.wordsapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


private const val LAYOUT_PREFERENCES_NAME = "layout_preferences"

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as receiver.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = LAYOUT_PREFERENCES_NAME)

class SettingsDataStore(context: Context) {

    private val IS_LINEAR_LAYOUT_MANAGER: Preferences.Key<Boolean> =
        booleanPreferencesKey("is_linear_layout_manager")

    // The edit method takes a block of code as parameter ( edit.(transform: suspend (MutablePreferences) -> Unit) )
    // Under the hood, the 'transform' block is treated as a single transaction, which is run on Dispatchers.IO
    // This is also why this function must be a suspend function
    suspend fun saveBooleanToPreferencesStore(isLinearLayoutManager: Boolean,
                                              context: Context) {
        context.dataStore.edit { preferences ->
            preferences[IS_LINEAR_LAYOUT_MANAGER] = isLinearLayoutManager
        }
    }

    // DataStore exposes the data stored in a Flow<Preferences> that emits every time a preference has changed.
    // You don't want to expose the entire Preferences object, just the Boolean value.
    // To do this, we map the Flow<Preferences>, and get the Boolean value you're interested in.
    val preferenceFlow: Flow<Boolean> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            // On the first run of the app, we will use LinearLayoutManager by default
            preferences[IS_LINEAR_LAYOUT_MANAGER] ?: true
        }

}
