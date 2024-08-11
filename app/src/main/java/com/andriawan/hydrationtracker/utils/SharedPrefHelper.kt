
package com.andriawan.hydrationtracker.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefHelper {

    private var sharedPreferences: SharedPreferences? = null

    private const val PREF_NAME = "hydration-preferences"
    const val PREF_DAILY_GOAL = "daily_goal"
    const val PREF_NOTIFICATION_ENABLED = "notification_enabled"
    const val PREF_NOTIFICATION_INTERVAL = "notification_interval"
    const val DEFAULT_DAILY_GOAL = 2700
    const val DEFAULT_NOTIFICATION_SETTING = true
    const val DEFAULT_NOTIFICATION_INTERVAL = 60 * 60 * 1000L // Default to 1 hour

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveString(key: String, value: String) {
        sharedPreferences?.edit()?.putString(key, value)?.apply()
    }

    fun readString(key: String, defaultValue: String): String {
        return sharedPreferences?.getString(key, defaultValue) ?: ""
    }

    fun saveInt(key: String, value: Int) {
        sharedPreferences?.edit()?.putInt(key, value)?.apply()
    }

    fun readInt(key: String, defaultValue: Int): Int {
        return sharedPreferences?.getInt(key, defaultValue) ?: 0
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences?.edit()?.putBoolean(key, value)?.apply()
    }

    fun readBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences?.getBoolean(key, defaultValue) ?: defaultValue
    }

    fun saveLong(key: String, value: Long) {
        sharedPreferences?.edit()?.putLong(key, value)?.apply()
    }

    fun readLong(key: String, defaultValue: Long): Long {
        return sharedPreferences?.getLong(key, defaultValue) ?: defaultValue
    }
}
