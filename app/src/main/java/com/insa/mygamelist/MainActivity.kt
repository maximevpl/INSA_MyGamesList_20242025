package com.insa.mygamelist
import Screen.GameDetailScreen
import Screen.GameListScreen
import Screen.GameListViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.ui.theme.MyGamesListTheme
import kotlinx.serialization.Serializable

/*
Activité principale de l'application
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Chargement des données JSON au démarrage de l'application
        IGDB.load(this)
        enableEdgeToEdge()
        setContent {
            MyGamesListTheme {
                val navController = rememberNavController()// Création du contrôleur de navigation
                val viewModel:GameListViewModel = viewModel()// Initialisation du ViewModel

                // Définition du NavHost pour gérer la navigation entre les écrans
                NavHost(navController, startDestination = GameListRoute) {
                    // Écran principal qui affiche la liste des jeux
                    composable<GameListRoute> {
                        GameListScreen(navController, viewModel)
                    }
                    //Écran secondaire qui affiche le détail d'un jeu
                    composable<GameDetailsRoute> { backStackEntry ->
                        val gameDetails = backStackEntry.toRoute<GameDetailsRoute>()
                        GameDetailScreen(gameDetails, navController)
                    }
                }
            }
        }
    }
}

// Définition des routes de navigation
@Serializable // Route pour la liste des jeux
object GameListRoute

@Serializable // Route pour le détail d'un jeu
data class GameDetailsRoute(val id: Long,
                            val cover: Long,
                            val first_release_date : Long,
                            val genres: List<Int>,
                            val name: String,
                            val platforms: List<Int>,
                            val summary: String,
                            val total_rating: Double,
                            val isFavorite:Boolean)




