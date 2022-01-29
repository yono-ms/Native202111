package com.example.native202111

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.native202111.ui.theme.Native202111Theme

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val welcomeDate by viewModel.welcomeDate.collectAsState()
    HomeContent(date = welcomeDate, onRefresh = { viewModel.refresh() })
}

@Composable
fun HomeContent(date: String, onRefresh: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Greeting(name = "HOME")
        Divider()
        Text(
            text = date,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Divider()
        Button(onClick = onRefresh, modifier = Modifier.align(Alignment.End)) {
            Text(text = "REFRESH")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Native202111Theme {
        HomeContent("Date Format", {})
    }
}