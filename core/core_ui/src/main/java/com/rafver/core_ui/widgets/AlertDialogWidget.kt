package com.rafver.core_ui.widgets

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AlertDialogWidget(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    confirmationText: String,
    dismissText: String,
    icon: ImageVector,
    iconContentDescription: String = "Alert Icon",
    canDismiss: Boolean = true,
) {
    AlertDialog(
        icon = { Icon(icon, contentDescription = iconContentDescription) },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = onDismissRequest,
        confirmButton = { TextButton(onClick = onConfirmation) { Text(confirmationText) } },
        dismissButton = {
            if(canDismiss) {
                TextButton(onClick = onDismissRequest) { Text(dismissText) }
            }
        }
    )
}