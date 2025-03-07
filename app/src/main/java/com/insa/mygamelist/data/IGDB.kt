package com.insa.mygamelist.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.insa.mygamelist.R
import kotlinx.serialization.Serializable

/*
Permet de récupérer tous les données présents dans les différents fichiers Json.
Stockage sous forme de liste d'éléments : covers, jeux, genres, logos de plateformes et plateformes.
 */
object IGDB {
    // Déclaration des listes stockant les données chargées depuis les fichiers JSON.
    lateinit var covers: List<Cover>
    lateinit var games: List<Game>
    lateinit var genres: List<Genre>
    lateinit var platform_logos: List<PlatformLogo>
    lateinit var platforms: List<Platform>


    /*
     Fonction permettant de récupérer les données à partir des fichiers JSON stockés dans res/raw.
     */
    fun load(context: Context) {
        // Lecture et conversion des fichiers JSON en listes d'objets correspondants.
        val coversFromJson: List<Cover> = Gson().fromJson(
            context.resources.openRawResource(R.raw.covers).bufferedReader(),
            object : TypeToken<List<Cover>>() {}.type
        )

        val gamesFromJson: List<Game> = Gson().fromJson(
            context.resources.openRawResource(R.raw.games).bufferedReader(),
            object : TypeToken<List<Game>>() {}.type
        )

        val genresFromJson: List<Genre> = Gson().fromJson(
            context.resources.openRawResource(R.raw.genres).bufferedReader(),
            object : TypeToken<List<Genre>>() {}.type
        )

        val platformLogosFromJson: List<PlatformLogo> = Gson().fromJson(
            context.resources.openRawResource(R.raw.platform_logos).bufferedReader(),
            object : TypeToken<List<PlatformLogo>>() {}.type
        )

        val platformsFromJson: List<Platform> = Gson().fromJson(
            context.resources.openRawResource(R.raw.platforms).bufferedReader(),
            object : TypeToken<List<Platform>>() {}.type
        )

        // Assignation des valeurs aux variables globales
        covers = coversFromJson
        games = gamesFromJson
        genres = genresFromJson
        platform_logos = platformLogosFromJson
        platforms = platformsFromJson
    }
}

// Définition des différentes classes de données représentant les entités du jeu.
@Serializable
data class Game(
    val id: Long,
    val cover: Long, // Identifiant de la couverture
    val first_release_date : Long,
    val genres: List<Int>, // Liste des identifiants de genre
    val name: String,
    val platforms: List<Int>, // Liste des identifiants de plateforme
    val summary: String,
    val total_rating: Double)

data class Genre (
    val id: Int,
    val name: String)

data class PlatformLogo (
    val id : Int,
    val url: String )

data class Platform (
    val id: Int,
    val name:String,
    val platform_logo: Int) // Identifiant du logo de la plateforme

data class Cover(
    val id: Long,
    val url: String)

/*
Fonction qui permet de récupérer l'url de la couverture à partir du numéro de
 cover présent dans le jeu.
 */
fun getCoverUrl(game : Game):String ?{
    //Recherche un numéro de cover d'un jeu identique à un id de cover.
    val cover = IGDB.covers.find{ it.id==game.cover}
    return cover?.url
}


/*
Fonction qui permet de récupérer la liste des genres pour un jeux donné.
Les genres sont récupéré dans une chaine de caractère, séparés par une virgule.
*/
fun getGenre (game : Game):String{
    var res: String =""
    for(i in game.genres){
        //Recherche un numéro de genre de la liste du jeu identique à un id de genre.
        val g = IGDB.genres.find{it.id==i}
        if(g!=null){    // Permet d'ajouter avant le genre si la liste n'est pas vide
            if(res.isNotEmpty()){
                res+=", "
            }
        }
        res+=g?.name
    }
    return res
}