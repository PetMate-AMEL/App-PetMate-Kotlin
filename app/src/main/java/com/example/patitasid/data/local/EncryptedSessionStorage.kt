package com.example.patitasid.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.patitasid.domain.model.Session

private const val FILE_NAME = "petmate_sesion"
private const val KEY_ACCESS = "token_acceso"
private const val KEY_REFRESH = "token_renovacion"
private const val KEY_HAS_BUSINESS = "tiene_negocio"

// Tokens are stored encrypted by the Android keystore, never in plain text (RNF-01, RNF-06).
class EncryptedSessionStorage(context: Context) : SessionStorage {
    private val preferences: SharedPreferences by lazy {
        val key = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        EncryptedSharedPreferences.create(
            context,
            FILE_NAME,
            key,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun read(): Session? {
        val accessToken = preferences.getString(KEY_ACCESS, null) ?: return null
        val refreshToken = preferences.getString(KEY_REFRESH, null) ?: return null
        return Session(
            accessToken = accessToken,
            refreshToken = refreshToken,
            hasBusiness = preferences.getBoolean(KEY_HAS_BUSINESS, false)
        )
    }

    override fun write(session: Session) {
        preferences.edit()
            .putString(KEY_ACCESS, session.accessToken)
            .putString(KEY_REFRESH, session.refreshToken)
            .putBoolean(KEY_HAS_BUSINESS, session.hasBusiness)
            .apply()
    }

    override fun erase() {
        preferences.edit().clear().apply()
    }
}
