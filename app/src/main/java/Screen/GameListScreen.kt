package Screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.insa.mygamelist.GameDetailsRoute
import com.insa.mygamelist.R
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.data.getCoverUrl
import com.insa.mygamelist.data.getGenre
import java.time.format.TextStyle

/*
Gestion de la page qui affiche la liste des jeux
*/

@OptIn(ExperimentalMaterial3Api::class)

/*
Fonction qui gère la liste des jeux en fonction des filtres
Elle appelle la fonction d'affichage pour chaque jeu séléctionné
*/
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
                title = { // Gestion de la barre de recherche si elle est actionnée
                    if(isSearching){
                        TextField(
                            value = searchText,
                            onValueChange = { viewModel.updateSearchText(it) },
                            placeholder = { Text("Rechercher un jeu...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                IconButton(onClick = { if(!searchText.isEmpty()){
                                    viewModel.updateSearchText("") //Mise à l'état initiale de la recherche
                                    }else{
                                        isSearching=false //Fermeture de la recherche
                                    }
                                }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Effacer")
                                }
                            }
                        )
                    }else{
                        Text("Liste des jeux") // Titre de la page
                    }
                },
                actions = { // Affichage de l'icone de recherche
                    IconButton(onClick = { isSearching=true}) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) { innerPadding ->
        /*
        Gestion du texte entrée dans la barre de recherche.
        Trie en fonction du nom, des genres et des plateformes du jeu.
         */
        val filteredGames = IGDB.games.filter{
            it.name.contains(searchText, ignoreCase = true) ||
                    getGenre(it).contains(searchText, ignoreCase = true) ||
                    getPlateform(it).contains(searchText, ignoreCase = true)
        }
        if(filteredGames.isEmpty()){ // Gestion d'une mauvaise recherche
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ){
                Text( // Affichage du message d'erreur
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
                items(filteredGames.size) { i -> // Appel de la fonction d'affichage pour chaque jeu
                    GameItem(filteredGames[i], navController)
                }
            }
        }
    }
}

/*
Fonction qui gère l'affichage d'une box d'un jeu
*/
@Composable
fun GameItem(game: Game,navController: NavController) {
    var isFavorite by rememberSaveable { mutableStateOf(false) } // Gestion des favoris
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
            // Affichage de la couverture
            AsyncImage(
                model = "https:" + getCoverUrl(game),
                contentDescription = "Image de" + game.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp)) // espace entre photo et texte

            Column {
                //Affichage du titre du jeu
                Text(
                    text = game.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold, // Texte en gras
                    textDecoration = TextDecoration.Underline, // Texte souligné
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(0.85f)
                )
                Spacer(modifier = Modifier.height(8.dp))

                //Affichage de la liste des genres du jeu
                Text(
                    text = "Genres : " + getGenre(game),
                    fontSize = 14.sp,
                    style = androidx.compose.ui.text.TextStyle(lineHeight = 16.sp),
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }

            //Affichage du bouton de favoris
            FavoriteButton(
                isFavorite = isFavorite,
                onFavoriteClicked = {  isFavorite= !isFavorite }
            )
        }
    }
}

/*
Fonction qui permet de récupérer la liste des plateform d'un jeu.
Les plateforme sont récupéré dans une chaine de caractère, séparés par une virgule.
*/
fun getPlateform (game : Game):String{
    var res: String =""
    for(i in game.platforms){
        val g = IGDB.platforms.find{it.id==i}
        if(g!=null){    // Permet d'ajouter avant la plateforme si la liste n'est pas vide
            if(res.isNotEmpty()){
                res+=", "
            }
        }
        res+=g?.name
    }
    return res
}

/*
Fonction qui permet de gérer l'icon des favoris et sa modification d'état
*/
@Composable
fun FavoriteButton(
    isFavorite:Boolean,
    onFavoriteClicked:(Boolean)->Unit,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF070707)
) {
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = { onFavoriteClicked(it) }
    ) {
        if (isFavorite) {
            Icon( //Icon plein
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = color
            )
        } else {
            Image( // Icon vide
                painter = painterResource(id = R.drawable.star_border),
                contentDescription = null
            )
        }
    }
}
