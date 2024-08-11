
package com.andriawan.hydrationtracker.ui.screens.settings

import com.andriawan.hydrationtracker.utils.SharedPrefHelper

data class SettingsState(
    val dailyGoals: Int = SharedPrefHelper.DEFAULT_DAILY_GOAL,
    val isNotificationEnabled: Boolean = SharedPrefHelper.DEFAULT_NOTIFICATION_SETTING,
    val notificationInterval: Long = SharedPrefHelper.DEFAULT_NOTIFICATION_INTERVAL
)
