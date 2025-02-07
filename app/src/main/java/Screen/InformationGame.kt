package Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.insa.mygamelist.GameDetailsRoute

@Composable
fun GameDetailScreen(game: GameDetailsRoute, innerPadding: PaddingValues){
    Column(modifier=Modifier.padding(innerPadding)){
        Text(text="Game:$game.id",
            fontSize = 24.sp)
    }
}