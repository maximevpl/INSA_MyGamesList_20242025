package Screen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@Composable
fun GameListScreen(innerPadding: PaddingValues, navController: NavController){
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ){
        items(IGDB.games.size){i ->
            GameItem(IGDB.games[i],navController)
        }
    }
}

@Composable
fun GameItem(game: Game,navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .clickable {navController.navigate(route= GameDetailsRoute(game.id,game.cover,game.first_release_date, game.genres, game.name, game.platforms, game.summary,game.total_rating)) }
            .padding(16.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            AsyncImage(     // affichage de la couverture
                model = "https:"+getCoverUrl(game),
                contentDescription = "Image de"+game.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp)) // espace entre photo et texte

            Column{
                Text(
                    text =game.name, // affiche le nom du jeu
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold, // Gras
                    textDecoration = TextDecoration.Underline, // Souligné
                    color = Color.Black
                )
                Text(
                    text = "Genres : "+ getGenre(game), // affiche la liste des genres
                    fontSize = 18.sp,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}