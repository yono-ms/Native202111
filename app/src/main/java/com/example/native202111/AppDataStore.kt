package com.example.native202111

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_datastore")

@Singleton
class AppDataStore @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore

    private object Keys {
        val USER_NAME = stringPreferencesKey("user_name")
    }

    val appPreferencesFlow: Flow<AppPreferences> = dataStore.data.map {
        AppPreferences(
            it[Keys.USER_NAME] ?: ""
        )
    }

    suspend fun setUserName(userName: String) {
        dataStore.edit {
            it[Keys.USER_NAME] = userName
        }
    }
}

data class AppPreferences(
    val userName: String,
)
