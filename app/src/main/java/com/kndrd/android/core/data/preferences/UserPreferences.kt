package com.kndrd.android.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        val KEY_USER_ID = stringPreferencesKey("user_id")
        val KEY_USER_NAME = stringPreferencesKey("user_name")
        val KEY_ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val KEY_INTERESTS_JSON = stringPreferencesKey("interests_json")
        val KEY_FCM_TOKEN = stringPreferencesKey("fcm_token")
    }

    val isOnboardingComplete: Flow<Boolean> =
        dataStore.data.map { it[KEY_ONBOARDING_DONE] ?: false }

    val currentUserId: Flow<String?> =
        dataStore.data.map { it[KEY_USER_ID] }

    val currentUserName: Flow<String?> =
        dataStore.data.map { it[KEY_USER_NAME] }

    suspend fun completeOnboarding(userId: String, name: String, interestsJson: String) {
        dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = userId
            prefs[KEY_USER_NAME] = name
            prefs[KEY_ONBOARDING_DONE] = true
            prefs[KEY_INTERESTS_JSON] = interestsJson
        }
    }

    suspend fun saveFcmToken(token: String) {
        dataStore.edit { it[KEY_FCM_TOKEN] = token }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
