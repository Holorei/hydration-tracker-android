package com.andriawan.hydrationtracker.ui.screens.settings

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.andriawan.hydrationtracker.utils.SharedPrefHelper
import com.andriawan.hydrationtracker.worker.NotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    var state by mutableStateOf(SettingsState())
        private set

    init {
        initData()
    }

    private fun initData() {
        Log.i("SettingsViewModel", "Initializing data...")
        val dailyGoals = SharedPrefHelper.readInt(
            SharedPrefHelper.PREF_DAILY_GOAL,
            SharedPrefHelper.DEFAULT_DAILY_GOAL
        )
        val isNotificationEnabled = SharedPrefHelper.readBoolean(
            SharedPrefHelper.PREF_NOTIFICATION_ENABLED,
            SharedPrefHelper.DEFAULT_NOTIFICATION_SETTING
        )
        val notificationInterval = SharedPrefHelper.readLong(
            SharedPrefHelper.PREF_NOTIFICATION_INTERVAL,
            SharedPrefHelper.DEFAULT_NOTIFICATION_INTERVAL
        )

        state = state.copy(
            dailyGoals = dailyGoals,
            isNotificationEnabled = isNotificationEnabled,
            notificationInterval = notificationInterval
        )

        if (isNotificationEnabled) {
            scheduleNotificationWork(notificationInterval)
        }
    }

    fun saveNewGoals(newGoals: Int) {
        Log.i("SettingsViewModel", "Saving new goals: $newGoals")
        SharedPrefHelper.saveInt(SharedPrefHelper.PREF_DAILY_GOAL, newGoals)
        initData()
    }

    fun toggleNotifications(isEnabled: Boolean) {
        Log.i("SettingsViewModel", "Toggling notifications: $isEnabled")
        SharedPrefHelper.saveBoolean(SharedPrefHelper.PREF_NOTIFICATION_ENABLED, isEnabled)
        if (isEnabled) {
            scheduleNotificationWork(state.notificationInterval)
        } else {
            WorkManager.getInstance(getApplication()).cancelUniqueWork("hydration_reminder")
        }
        initData()
    }

    fun setNotificationInterval(interval: Long) {
        Log.i("SettingsViewModel", "Setting notification interval: ${interval / (60 * 1000)} minutes")
        SharedPrefHelper.saveLong(SharedPrefHelper.PREF_NOTIFICATION_INTERVAL, interval)
        scheduleNotificationWork(interval)
        initData()
    }

    private fun scheduleNotificationWork(interval: Long) {
        Log.i("SettingsViewModel", "Scheduling notification work with interval: ${interval / (60 * 1000)} minutes")
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            interval, TimeUnit.MILLISECONDS
        ).build()

        WorkManager.getInstance(getApplication())
            .enqueueUniquePeriodicWork(
                "hydration_reminder",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
    }
}
