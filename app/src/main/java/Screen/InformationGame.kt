package Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

@OptIn(ExperimentalMaterial3Api::class)
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
                        Text(gameDetails.name,
                            modifier = Modifier.fillMaxWidth(0.85f))
                        FavoriteButton(
                            isFavorite = isFavorite,
                            onFavoriteClicked = { isFavorite = !isFavorite }
                        )
                    }
                        },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector= Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription ="Retour"
                        )
                    }
                }

            )
        },
        modifier = Modifier.fillMaxWidth()
    ){innerPadding ->

    val scrollState = rememberScrollState() // initialise le scrollState

    Column(modifier = Modifier.fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(scrollState) // défilement vertical
        .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),

    horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.padding(top = 15.dp))
        // affichage du titre

        Text(
                text = "${gameDetails.name}",
                fontWeight = FontWeight.Bold, // Gras
                textDecoration = TextDecoration.Underline, // Souligné
                fontSize = 24.sp,
        )



        Spacer(modifier = Modifier.padding(top = 30.dp))
        // affichage de la couverture
        AsyncImage(
            model = "https:" + getCoverUrl2(gameDetails),
            contentDescription = "Image de" + gameDetails.name,
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.padding(top = 10.dp))
        // liste des genres du jeu
        Text(
            text = getGenre2(gameDetails),
            fontStyle = FontStyle.Italic,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.padding(top = 30.dp))
        // bar avec logo des plateformes
        // revoir avec peut être une barre qui coulisse, taille des logos ?, et si pas de logos
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())// défilement horizontal
                .padding(16.dp),

            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            for (i in gameDetails.platforms.indices) {
                AsyncImage(
                    model = "https:" + getPlateformLogo(gameDetails, i),
                    contentDescription = "Image de" + gameDetails.name, // à modifier
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
        Spacer(modifier = Modifier.padding(top = 30.dp))
        Text(
            text = gameDetails.summary,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp)
        )
    }



    }
}

fun getCoverUrl2(gameDetails : GameDetailsRoute):String ?{
    val cover = IGDB.covers.find{ it.id==gameDetails.cover}
    return cover?.url
}

fun getPlateformLogo(gameDetails: GameDetailsRoute, i : Int):String ?{
    val platform = IGDB.platforms.find{it.id==gameDetails.platforms[i]}
    val platformLogo = IGDB.platform_logos.find{it.id== platform?.platform_logo }
    return platformLogo?.url?:"//cdn-icons-png.flaticon.com/512/17344/17344604.png"
}

fun getGenre2 (gameDetails : GameDetailsRoute):String{
    var res: String =""
    for(i in gameDetails.genres){
        val g = IGDB.genres.find{it.id==i}
        if(g!=null){    // permet d'ajouter avant le genre si la liste n'est pas vide
            if(res.isNotEmpty()){
                res+=", "
            }
        }
        res+=g?.name
    }
    return res
}