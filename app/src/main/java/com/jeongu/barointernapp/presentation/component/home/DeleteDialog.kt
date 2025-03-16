package com.jeongu.barointernapp.presentation.component.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jeongu.barointernapp.R
import com.jeongu.barointernapp.presentation.model.ProductModel

@Composable
fun DeleteDialog(
    product: ProductModel,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_title_delete)) },
        text = { Text(stringResource(R.string.dialog_message_delete, product.title)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.btn_label_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_label_cancel))
            }
        }
    )
}