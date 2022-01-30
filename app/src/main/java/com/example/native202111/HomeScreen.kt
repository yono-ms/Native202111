package com.example.native202111

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.native202111.network.RepoModel
import com.example.native202111.ui.theme.Native202111Theme

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val welcomeDate by viewModel.welcomeDate.collectAsState()
    val showInputDialog by viewModel.showInputDialog.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val repoItems by viewModel.repoItems.collectAsState()
    HomeContent(
        date = welcomeDate,
        onRefresh = { viewModel.refresh() },
        onClickUserName = { viewModel.editUserName() },
        showInputDialog = showInputDialog,
        onCancelEditUserName = { viewModel.cancelEditUserName() },
        onConfirmEditUserName = { viewModel.confirmEditUserName(it) },
        userName = userName,
        repoItems = repoItems
    )
}

@Composable
fun HomeContent(
    date: String,
    onRefresh: () -> Unit,
    onClickUserName: () -> Unit,
    showInputDialog: Boolean,
    onCancelEditUserName: () -> Unit,
    onConfirmEditUserName: (userName: String) -> Unit,
    userName: String,
    repoItems: List<RepoModel>
) {
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
        Divider()
        Row(
            modifier = Modifier
                .clickable(onClick = onClickUserName)
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
                .requiredWidthIn(min = 200.dp, max = Dp.Unspecified)
                .border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            Text(text = userName, modifier = Modifier.padding(16.dp))
        }
        Divider()
        LazyColumn(
            contentPadding = PaddingValues(0.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(repoItems, key = { item -> item.name }) { repoItem ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = repoItem.name, fontSize = 16.sp)
                    Text(text = repoItem.updatedAt, fontSize = 10.sp)
                }
            }
        }
    }
    InputDialogScreen(
        showDialog = showInputDialog,
        value = userName,
        title = "Input UserName",
        onCancel = onCancelEditUserName,
        onOk = onConfirmEditUserName
    )
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Native202111Theme {
        HomeContent("Date Format", {}, {}, false, {}, {}, "user name", listOf(
            RepoModel("Name1", "date1"),
            RepoModel("Name2", "date2"),
            RepoModel("Name3", "date3"),
        )
        )
    }
}