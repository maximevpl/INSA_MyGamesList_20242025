package com.insa.mygamelist.ui.theme

import com.insa.mygamelist.data.Game

class FavSet {
    var favSet = mutableSetOf<Long>()

    fun add (game : Game){
        favSet.add(game.id)
    }

    fun remove (game : Game){
        favSet.remove(game.id)
    }

    fun isFav(game : Game): Boolean{
        return favSet.contains(game.id)
    }
}