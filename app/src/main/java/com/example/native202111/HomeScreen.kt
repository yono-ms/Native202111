package com.example.native202111

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.native202111.ui.theme.Native202111Theme

@Composable
fun HomeScreen() {
    HomeContent()
}

@Composable
fun HomeContent() {
    Greeting(name = "HOME")
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Native202111Theme {
        HomeContent()
    }
}