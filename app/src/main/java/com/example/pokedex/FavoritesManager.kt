package com.example.pokedex

import android.content.Context

object FavoritesManager {
    private const val PREFS = "pokedex_prefs"
    private const val KEY = "favorites"

    // Récupère l'ensemble des noms favoris
    fun getFavorites(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY, emptySet()) ?: emptySet()
    }

    fun isFavorite(context: Context, name: String): Boolean {
        return getFavorites(context).contains(name)
    }

    // Ajoute ou retire un favori, puis sauvegarde
    fun toggle(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val current = getFavorites(context).toMutableSet()
        if (current.contains(name)) current.remove(name) else current.add(name)
        prefs.edit().putStringSet(KEY, current).apply()
    }
}