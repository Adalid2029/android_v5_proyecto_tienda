package com.version5.tiendavirtual.data.almacenamiento

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow

class TokenManager(private val context: Context) {
    companion object {
        private const val NOMBRE_DATASTORAGE = "token_preferences"

        private val KEY_TOKEN = stringPreferencesKey("jwt_token")
        private val KEY_USUARIO_ID = stringPreferencesKey("usuario_id")
        private val KEY_NOMBRE_USUARIO = stringPreferencesKey("nombre_usuario")
        private val KEY_EMAIL = stringPreferencesKey("email")
        private val KEY_NOMBRE_COMPLETO = stringPreferencesKey("nombre_completo")
        private val KEY_EXPIRA_EN = stringPreferencesKey("expira_en")

        private val Context.datastorage: DataStore<Preferences> by preferencesDataStore(
            name = NOMBRE_DATASTORAGE
        )
    }

    suspend fun guardarDatosSesion(
        token: String,
        usuarioId: String,
        nombreUsuario: String,
        email: String,
        nombreCompleto: String,
        expiraEn: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = "Bearer $token"
            preferences[KEY_USUARIO_ID] = usuarioId
            preferences[KEY_NOMBRE_USUARIO] = nombreUsuario
            preferences[KEY_EMAIL] = email
            preferences[KEY_NOMBRE_COMPLETO] = nombreCompleto
            preferences[KEY_EXPIRA_EN] = expiraEn
        }
    }

    fun estaLogueado(): Flow<Boolean>{
        return context.dataStore.data.map{ preferences->
            preferences[KEY_TOKEN] != null
        }
    }
}