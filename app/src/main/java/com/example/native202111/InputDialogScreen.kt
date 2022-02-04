package com.example.native202111

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.native202111.ui.theme.Native202111Theme
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Composable
fun InputDialogScreen(
    value: String,
    title: String,
    onCancel: () -> Unit,
    onOk: (newValue: String) -> Unit,
    viewModel: InputDialogViewModel = hiltViewModel()
) {
    val logger: Logger by lazy { LoggerFactory.getLogger("InputDialog") }

    val inputValue by viewModel.inputValue.collectAsState()

    viewModel.initInputValue(value)

    InputDialogContent(
        title = title,
        onOk = {
            onOk(inputValue)
            viewModel.clearInputValue()
        },
        onCancel = {
            onCancel()
            viewModel.clearInputValue()
        },
        inputValue = inputValue,
        onValueChange = {
            logger.debug("onValueChange $it")
            viewModel.setInputValue(it)
        }
    )
}

@Composable
fun InputDialogContent(
    title: String,
    onOk: () -> Unit,
    onCancel: () -> Unit,
    inputValue: String,
    onValueChange: (newValue: String) -> Unit,
) {
    val okText = stringResource(id = android.R.string.ok)
    val cancelText = stringResource(id = android.R.string.cancel)

    AlertDialog(
        onDismissRequest = { /* ignore */ },
        confirmButton = {
            TextButton(onClick = onOk) {
                Text(text = okText)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = cancelText)
            }
        },
        title = { Text(text = "Text Input Dialog") },
        text = {
            TextField(
                value = inputValue,
                onValueChange = onValueChange,
                label = { Text(text = title) },
                placeholder = { Text(text = "your login name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun InputDialogScreenPreview() {
    Native202111Theme {
        InputDialogContent("title", {}, {}, "inputValue", {})
    }
}
