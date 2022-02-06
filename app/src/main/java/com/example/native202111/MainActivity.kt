package com.example.native202111

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.native202111.ui.theme.Native202111Theme
import dagger.hilt.android.AndroidEntryPoint
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logger.info("onCreate savedInstanceState=$savedInstanceState")

        setContent {
            Native202111Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

enum class DestRoute {
    Home,
    DataCheck,
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = DestRoute.Home.name) {
        composable(DestRoute.Home.name) {
            HomeScreen(navController)
        }
        composable(DestRoute.DataCheck.name) {
            DataCheckScreen(navController)
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Native202111Theme {
        Greeting("Android")
    }
}