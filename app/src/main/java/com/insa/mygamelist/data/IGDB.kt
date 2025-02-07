package com.insa.mygamelist.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.insa.mygamelist.R
import kotlinx.serialization.Serializable

/*
Permet de récupérer tous les données présents dans les différents
fichiers Json

 */
object IGDB {

    lateinit var covers: List<Cover>
    lateinit var games: List<Game>
    lateinit var genres: List<Genre>
    lateinit var platform_logos: List<PlatformLogo>
    lateinit var platforms: List<Platform>

    fun load(context: Context) {
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


        covers = coversFromJson
        games = gamesFromJson
        genres = genresFromJson
        platform_logos = platformLogosFromJson
        platforms = platformsFromJson
    }
}

data class Cover(
    val id: Long,
    val url: String)

@Serializable
data class Game(
    val id: Long,
    val cover: Long,
    val first_release_date : Long,
    val genres: List<Int>,
    val name: String,
    val platforms: List<Int>,
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
    val platform_logo: Int)


/*Fonction qui permet de récupérer l'url à partir du numéro
* présent dans game
 */

fun getCoverUrl(game : Game):String ?{
    val cover = IGDB.covers.find{ it.id==game.cover}
    return cover?.url
}

/* Fonction qui permet de récupérer la liste des genres
* pour un jeux donné.
* Chaque genre est séparé par une virgule
*/
fun getGenre (game : Game):String{
    var res: String =""
    for(i in game.genres){
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