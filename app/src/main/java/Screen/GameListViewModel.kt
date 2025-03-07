package Screen

import androidx.lifecycle.ViewModel
import com.insa.mygamelist.data.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameListViewModel : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()


    // Liste des jeux favoris
    private val _favoriteGames = MutableStateFlow<List<Game>>(emptyList())
    val favoriteGames = _favoriteGames.asStateFlow()

    fun updateSearchText(newText:String){
        _searchText.value=newText
    }

    // Mise à jour de l'état des favoris
    fun toggleFavorite(game: Game) {
        val updatedList = if (_favoriteGames.value.contains(game)) {
            _favoriteGames.value.filter { it != game }
        } else {
            _favoriteGames.value + game
        }
        _favoriteGames.value = updatedList
    }
}