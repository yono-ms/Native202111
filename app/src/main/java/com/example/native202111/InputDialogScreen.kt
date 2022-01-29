package com.example.native202111

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.native202111.ui.theme.Native202111Theme
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Composable
fun InputDialogScreen(
    showDialog: Boolean,
    value: String,
    title: String,
    onCancel: () -> Unit,
    onOk: (newValue: String) -> Unit
) {
    if (showDialog) {

        val logger: Logger by lazy { LoggerFactory.getLogger("InputDialog") }

        val okText = stringResource(id = android.R.string.ok)
        val cancelText = stringResource(id = android.R.string.cancel)

        var inputValue by remember { mutableStateOf(value) }

        AlertDialog(
            onDismissRequest = { /* ignore */ },
            confirmButton = {
                TextButton(onClick = { onOk(inputValue) }) {
                    Text(text = okText)
                }
            },
            dismissButton = {
                TextButton(onClick = onCancel) {
                    Text(text = cancelText)
                }
            },
            title = { Text(text = title) },
            text = {
                TextField(value = inputValue, onValueChange = {
                    logger.debug("onValueChange $it")
                    inputValue = it
                })
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputDialogScreenPreview() {
    Native202111Theme {
        InputDialogScreen(true, "value", "title", {}, {})
    }
}
