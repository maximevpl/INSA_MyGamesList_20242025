package com.insa.mygamelist
import Screen.GameDetailScreen
import Screen.GameListScreen
import Screen.GameListViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.ui.theme.MyGamesListTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {
            MyGamesListTheme {
                val navController = rememberNavController()
                val viewModel:GameListViewModel = viewModel()

                NavHost(navController, startDestination = GameListRoute) {
                    composable<GameListRoute> {
                        GameListScreen(navController, viewModel)
                    }
                    composable<GameDetailsRoute> { backStackEntry ->
                        val gameDetails = backStackEntry.toRoute<GameDetailsRoute>()
                        GameDetailScreen(gameDetails, navController)
                    }
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
                            val total_rating: Double,
                            val isFavorite:Boolean)




