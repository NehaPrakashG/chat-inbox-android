package com.example.chatinbox.ui.components

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

/**
 * Handles back press on the root screen and shows a confirmation dialog
 * before exiting the application.
 */
@Composable
fun ExitHandler() {
    var showExitDialog by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        ExitDialog(
            onConfirmExit = {
                showExitDialog = false
                activity?.finishAndRemoveTask()
            },
            onDismiss = {
                showExitDialog = false
            }
        )
    }
}

@Composable
private fun ExitDialog(
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Exit App") },
        text = { Text("Are you sure you want to exit the app?") },
        confirmButton = {
            TextButton(onClick = onConfirmExit) {
                Text("Exit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}