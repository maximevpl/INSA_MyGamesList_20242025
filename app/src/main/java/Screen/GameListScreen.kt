package Screen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.insa.mygamelist.GameDetailsRoute
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.data.getCoverUrl
import com.insa.mygamelist.data.getGenre

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListScreen(navController: NavController,viewModel: GameListViewModel ){
    val searchText by viewModel.searchText.collectAsState()
    var isSearching by remember {mutableStateOf(false)}

    Scaffold(
        topBar = {
            Column {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.Magenta,
                    titleContentColor = Color.Black,
                ),
                title = {
                    if(isSearching){
                        TextField(
                            value = searchText,
                            onValueChange = { viewModel.updateSearchText(it) },
                            placeholder = { Text("Rechercher un jeu...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                IconButton(onClick = { viewModel.updateSearchText("") }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Effacer")
                                }
                            }
                        )
                    }else{
                        Text("Liste des jeux")
                    }

                },
                actions = {
                    IconButton(onClick = { isSearching=true}) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) { innerPadding ->
        val filteredGames = IGDB.games.filter{
            it.name.contains(searchText, ignoreCase = true) ||
                    getGenre(it).contains(searchText, ignoreCase = true) ||
                    getPlateform(it).contains(searchText, ignoreCase = true)
        }
        if(filteredGames.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "No match :(",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

        }else{
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                items(filteredGames.size) { i ->
                    GameItem(filteredGames[i], navController)
                }
            }
        }

    }
}

@Composable
fun GameItem(game: Game,navController: NavController) {
    var isFavorite by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .clickable {

                navController.navigate(
                    route = GameDetailsRoute(
                        game.id,
                        game.cover,
                        game.first_release_date,
                        game.genres,
                        game.name,
                        game.platforms,
                        game.summary,
                        game.total_rating,
                        isFavorite
                    )
                )
            }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(     // affichage de la couverture
                model = "https:" + getCoverUrl(game),
                contentDescription = "Image de" + game.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp)) // espace entre photo et texte

            Column {
                Text(
                    text = game.name, // affiche le nom du jeu
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold, // Gras
                    textDecoration = TextDecoration.Underline, // Souligné
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(0.85f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Genres : " + getGenre(game), // affiche la liste des genres
                    fontSize = 14.sp,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(0.85f)
                )
            }
            FavoriteButton(
                isFavorite = isFavorite,
                onFavoriteClicked = { isFavorite= !isFavorite }
            )
        }
    }
}


fun getPlateform (game : Game):String{
    var res: String =""
    for(i in game.platforms){
        val g = IGDB.platforms.find{it.id==i}
        if(g!=null){    // permet d'ajouter avant la plateform si la liste n'est pas vide
            if(res.isNotEmpty()){
                res+=", "
            }
        }
        res+=g?.name
    }
    return res
}

@Composable
fun FavoriteButton(
    isFavorite:Boolean,
    onFavoriteClicked:(Boolean)->Unit,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF1C040C)
) {
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = { onFavoriteClicked(it) }
    ) {
        Icon(
            tint = color,
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }

}
