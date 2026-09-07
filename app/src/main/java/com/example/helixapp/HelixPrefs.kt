package com.example.helixapp

import android.content.Context
import android.os.Build
import java.util.UUID

object HelixPrefs {
    private const val PREFS = "helix_prefs"
    private const val KEY_BASE_URL = "base_url"
    private const val KEY_SESSION_TOKEN = "mr_session"
    private const val KEY_USERNAME = "username"
    private const val KEY_LAST_STATION_NAME = "last_station_name"
    private const val KEY_DEVICE_ID = "device_id"

    /** Stable renderer identity reported to Helix via X-Helix-Device-* headers. */
    const val DEVICE_KIND = "android"

    fun getDeviceId(context: Context): String {
        val p = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        p.getString(KEY_DEVICE_ID, null)?.let { if (it.isNotBlank()) return it }
        val generated = UUID.randomUUID().toString()
        p.edit().putString(KEY_DEVICE_ID, generated).apply()
        return generated
    }

    fun getDeviceName(context: Context): String {
        val manufacturer = Build.MANUFACTURER.trim()
        val model = Build.MODEL.trim()
        val name = buildString {
            if (manufacturer.isNotBlank()) append(manufacturer)
            if (manufacturer.isNotBlank() && model.isNotBlank()) append(' ')
            if (model.isNotBlank()) append(model)
        }.trim()
        return name.ifBlank { "Helix Android" }
    }

    fun getBaseUrl(context: Context, defaultValue: String = "http://192.168.0.96:10011"): String {
        val p = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return p.getString(KEY_BASE_URL, defaultValue) ?: defaultValue
    }

    fun setBaseUrl(context: Context, value: String) {
        val v = value.trim().trimEnd('/')
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_BASE_URL, v)
            .apply()
    }

    fun getSessionToken(context: Context): String? {
        val p = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return p.getString(KEY_SESSION_TOKEN, null)
    }

    fun setSessionToken(context: Context, token: String?) {
        val e = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
        if (token.isNullOrBlank()) e.remove(KEY_SESSION_TOKEN) else e.putString(KEY_SESSION_TOKEN, token)
        e.apply()
    }

    fun getUsername(context: Context): String? {
        val p = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return p.getString(KEY_USERNAME, null)
    }

    fun setUsername(context: Context, username: String?) {
        val e = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
        if (username.isNullOrBlank()) e.remove(KEY_USERNAME) else e.putString(KEY_USERNAME, username)
        e.apply()
    }

    fun clearAuth(context: Context) {
        setSessionToken(context, null)
        setUsername(context, null)
    }

    fun getLastStationName(context: Context): String? {
        val p = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return p.getString(KEY_LAST_STATION_NAME, null)
    }

    fun setLastStationName(context: Context, stationName: String?) {
        val e = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
        if (stationName.isNullOrBlank()) e.remove(KEY_LAST_STATION_NAME) else e.putString(KEY_LAST_STATION_NAME, stationName)
        e.apply()
    }
}
