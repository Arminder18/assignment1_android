package com.example.assignment1_android

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    companion object {

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_data")

        // Define the keys for the preferences
        private val ID_KEY = stringPreferencesKey("id_key")
        private val USERNAME_KEY = stringPreferencesKey("username_key")
        private val COURSE_NAME_KEY = stringPreferencesKey("course_name_key")
    }

    // Save student details (ID, Username, Course Name)
    suspend fun storeData(id: String, username: String, courseName: String) {
        context.dataStore.edit { preferences ->
            preferences[ID_KEY] = id
            preferences[USERNAME_KEY] = username
            preferences[COURSE_NAME_KEY] = courseName
        }
    }

    // Get student ID from DataStore
    val getId: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[ID_KEY] ?: "" }

    // Get username from DataStore
    val getUserName: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[USERNAME_KEY] ?: "" }

    // Get course name from DataStore
    val getCourseName: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[COURSE_NAME_KEY] ?: "" }

    // Reset student details
    suspend fun resetData() {
        context.dataStore.edit { preferences ->
            preferences.remove(ID_KEY)
            preferences.remove(USERNAME_KEY)
            preferences.remove(COURSE_NAME_KEY)
        }
    }
}
