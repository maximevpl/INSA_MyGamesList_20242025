package Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.insa.mygamelist.GameDetailsRoute
import com.insa.mygamelist.data.IGDB

/*
Gestion de la page qui détail les informations détaillées d'un jeu.
*/
@OptIn(ExperimentalMaterial3Api::class)

/*
Fonction qui permet d'afficher les détails d'un jeu : titre, couverture, genres, plateformes
et résumé.
 */
@Composable
fun GameDetailScreen(gameDetails: GameDetailsRoute,navController: NavController){
    var isFavorite by remember { mutableStateOf(gameDetails.isFavorite) } // Utilise l'état passé
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.Magenta,
                    titleContentColor = Color.Black,
                ),
                title = {
                    Row(modifier = Modifier.padding(16.dp)){
                        Text(gameDetails.name, // Titre du jeu dans l'AppBar
                            modifier = Modifier.fillMaxWidth(0.85f))
                        FavoriteButton(
                            isFavorite = isFavorite, // Gestion des favoris
                            onFavoriteClicked = { isFavorite = !isFavorite }
                        )
                    }
                        },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector= Icons.AutoMirrored.Filled.ArrowBack, // Flèche de retour
                            contentDescription ="Retour"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxWidth()
    ) { innerPadding ->
        val scrollState = rememberScrollState() // Initialisation du scrollState
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState) // Défilement vertical
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(top = 15.dp))

            // Affichage du titre du jeu
            Text(
                text = "${gameDetails.name}",
                fontWeight = FontWeight.Bold, // Gras
                textDecoration = TextDecoration.Underline, // Souligné
                fontSize = 24.sp,
            )
            Spacer(modifier = Modifier.padding(top = 30.dp))

            // Affichage de la couverture
            AsyncImage(
                model = "https:" + getCoverUrl2(gameDetails),
                contentDescription = "Image de" + gameDetails.name,
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.padding(top = 10.dp))

            // Affichage de la liste des genres du jeu.
            Text(
                text = getGenre2(gameDetails),
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(20.dp))

            // Affichage de la bar défilante avec logo des plateformes du jeu.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())// Défilement horizontal
                    .padding(16.dp),
            ) {
                for (i in gameDetails.platforms.indices) {
                    AsyncImage(
                        model = "https:" + getPlateformLogo(gameDetails, i),
                        contentDescription = "Image de" + gameDetails.name, // à modifier
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black)
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))

            //Affichage du sommaire du jeu.
            Text(
                text = gameDetails.summary,
                fontSize = 18.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

/*
Fonction qui permet de récupérer le lien d'une couverture d'un jeu.
*/
fun getCoverUrl2(gameDetails : GameDetailsRoute):String ?{
    //Recherche un numéro de cover d'un jeu identique à un id de cover.
    val cover = IGDB.covers.find{ it.id==gameDetails.cover}
    return cover?.url
}

/*
Fonction qui permet de récupérer le lien d'une plateforme.
Retourne le lien de la photo de la plateforme ou un lien d'une image par défault sinon.
*/
fun getPlateformLogo(gameDetails: GameDetailsRoute, i : Int):String ?{
    val platform = IGDB.platforms.find{it.id==gameDetails.platforms[i]}
    val platformLogo = IGDB.platform_logos.find{it.id== platform?.platform_logo }
    return platformLogo?.url?:"//cdn-icons-png.flaticon.com/512/17344/17344604.png"
}

/*
Fonction qui permet de récupérer la liste des genres pour un jeux donné.
Les genres sont récupéré dans une chaine de caractère, séparés par une virgule.
*/
fun getGenre2 (gameDetails : GameDetailsRoute):String{
    var res: String =""
    for(i in gameDetails.genres){
        //Recherche un numéro de genre de la liste du jeu identique à un id de genre.
        val g = IGDB.genres.find{it.id==i}
        if(g!=null){    // Permet d'ajouter avant le genre si la liste n'est pas vide.
            if(res.isNotEmpty()){
                res+=", "
            }
        }
        res+=g?.name
    }
    return res
}