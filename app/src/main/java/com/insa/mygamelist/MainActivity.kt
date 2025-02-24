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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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
            val navController = rememberNavController()
            var selectedTitle by remember { mutableStateOf("My Games List") }

            val currentRoute = navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry).value?.destination?.route
            if(currentRoute=="GameListRoute"){
                selectedTitle ="My Games List"
            }

            MyGamesListTheme {
                Scaffold(
                    topBar = {
                    TopAppBar(
                        colors = topAppBarColors(
                        containerColor = Color.Magenta,
                        titleContentColor = Color.Black,
                    ),
                        title = { Text(selectedTitle) }
                        )
                }, modifier = Modifier.fillMaxWidth()) { innerPadding ->
                    Navigation(innerPadding){ newTitle ->
                        selectedTitle = newTitle
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
                            val total_rating: Double)


@Composable
fun Navigation(innerPadding:PaddingValues, onTitleChange:(String) ->Unit){
    val navController = rememberNavController()

    NavHost(navController, startDestination = GameListRoute){
        composable<GameListRoute> {
            onTitleChange("My Games List") // Réinitialise le titre en revenant sur la page d'acceuil
            GameListScreen(innerPadding, navController, onTitleChange)
        }
        composable<GameDetailsRoute> { backStackEntry ->
            val gameDetails = backStackEntry.toRoute<GameDetailsRoute>()
            onTitleChange(gameDetails.name)
            GameDetailScreen(gameDetails, innerPadding )
        }
    }


}

