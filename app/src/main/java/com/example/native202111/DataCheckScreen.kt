package com.example.native202111

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.native202111.database.RepoEntity
import com.example.native202111.database.UserEntity
import com.example.native202111.ui.theme.Native202111Theme

@Composable
fun DataCheckScreen(
    navController: NavController,
    viewModel: DataCheckViewModel = hiltViewModel()
) {
    val showUser by viewModel.showUser.collectAsState()
    val users by viewModel.userItems.collectAsState()
    val repos by viewModel.repoItems.collectAsState()

    DataCheckContent(
        onBack = { navController.popBackStack() },
        showUser = showUser,
        onShowUserChange = { value -> viewModel.setShowUser(value) },
        users = users,
        repos = repos
    )
}

@Composable
fun DataCheckContent(
    onBack: () -> Unit,
    showUser: Boolean,
    onShowUserChange: (value: Boolean) -> Unit,
    users: List<UserEntity>,
    repos: List<RepoEntity>
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Greeting(name = "DATA CHECK")
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBack) {
            Text(text = "BACK")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Row(verticalAlignment = CenterVertically) {
            Checkbox(checked = showUser, onCheckedChange = { onShowUserChange(!showUser) })
            Text(text = "user_table")
        }
        if (showUser) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(users, key = { item -> item.id }) { user ->
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Row {
                            Text(text = user.userName, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = user.updateAtText,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(CenterVertically),
                                textAlign = TextAlign.End,
                                fontSize = 10.sp
                            )
                        }
                        Text(text = user.reposUrl, fontSize = 10.sp)
                    }
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(repos) { repo ->
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = repo.name, fontSize = 16.sp)
                        Row {
                            Text(text = repo.updateAtText, fontSize = 10.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = repo.userName,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DataCheckPreview() {
    Native202111Theme {
        DataCheckContent(
            {},
            true,
            {},
            listOf(
                UserEntity(1, "name1", 1, "update1", "url1"),
                UserEntity(2, "name2", 2, "update2", "url2"),
            ),
            listOf(
                RepoEntity(1, "name1", 1, "update1", 1, "uname1"),
                RepoEntity(2, "name2", 2, "update2", 1, "uname2"),
            )
        )
    }
}
