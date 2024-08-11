package com.andriawan.hydrationtracker.ui.screens.settings

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andriawan.hydrationtracker.R
import com.andriawan.hydrationtracker.ui.components.DialogEditValue
import com.andriawan.hydrationtracker.ui.components.Setting
import com.andriawan.hydrationtracker.ui.components.SettingHeader

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state = viewModel.state
    var showDialog by remember { mutableStateOf(false) }
    var showIntervalDialog by remember { mutableStateOf(false) }
    var intervalInput by remember { mutableStateOf((state.notificationInterval / (60 * 1000)).toString()) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SettingsPageTopBar()
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SettingHeader(title = stringResource(id = R.string.application_settings))
            Column {
                Setting(
                    title = stringResource(id = R.string.settings_goal_amount),
                    value = state.dailyGoals.toString(),
                    onItemClicked = {
                        showDialog = !showDialog
                    }
                )
                Setting(
                    title = stringResource(id = R.string.settings_notifications),
                    value = if (state.isNotificationEnabled) "Enabled" else "Disabled",
                    onItemClicked = {
                        viewModel.toggleNotifications(!state.isNotificationEnabled)
                    }
                )
                Setting(
                    title = "Notification Interval",
                    value = "${state.notificationInterval / (60 * 1000)} minutes",
                    onItemClicked = {
                        showIntervalDialog = !showIntervalDialog
                    }
                )
            }
        }
    }

    if (showDialog) {
        DialogEditValue(
            title = stringResource(id = R.string.settings_edit_goal_amount),
            value = state.dailyGoals.toString(),
            onSubmit = {
                val newValue = it.toInt()
                viewModel.saveNewGoals(newValue)
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            }
        )
    }

    if (showIntervalDialog) {
        DialogEditValue(
            title = "Set Notification Interval (in minutes)",
            value = intervalInput,
            onSubmit = {
                val newInterval = it.toLongOrNull()?.times(60 * 1000) ?: state.notificationInterval
                viewModel.setNotificationInterval(newInterval)
                intervalInput = (newInterval / (60 * 1000)).toString()
                showIntervalDialog = false
            },
            onDismiss = {
                showIntervalDialog = false
            }
        )
    }
}



@Composable
fun SettingsPageTopBar() {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.settings))
        }
    )
}
