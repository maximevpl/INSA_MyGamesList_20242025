package com.insa.mygamelist


import Screen.GameDetailScreen
import Screen.GameListScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.ui.theme.MyGamesListTheme
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {

            MyGamesListTheme {
                Scaffold(topBar = {
                    TopAppBar(colors = topAppBarColors(
                        containerColor = Color.Magenta,
                        titleContentColor = Color.Black,
                    ), title = { Text("My Games List") })
                }, modifier = Modifier.fillMaxWidth()) { innerPadding ->
                    Navigation(innerPadding)

                }
            }
        }
    }
}


@Serializable
object GameListRoute
@Serializable
data class GameDetailsRoute(val id: Long,
                            val cover: Long,
                            val first_release_date : Long,
                            val genres: List<Int>,
                            val name: String,
                            val platforms: List<Int>,
                            val summary: String,
                            val total_rating: Double)


@Composable
fun Navigation(innerPadding:PaddingValues){
    val navController = rememberNavController()

    NavHost(navController, startDestination=GameListRoute){
        composable<GameListRoute>{
            GameListScreen(innerPadding,navController)
        }
        composable<GameDetailsRoute> {backStackEntry ->
            val gamedetailsRoute = backStackEntry.toRoute<GameDetailsRoute>()
            GameDetailScreen(
                game=gamedetailsRoute,innerPadding // problème avec id de gamedetailsroute
            )
        }
    }


}

