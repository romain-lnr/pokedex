package com.example.pokedex

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: PokemonAdapter
    private var fullList: List<PokemonEntry> = emptyList()
    private var currentQuery: String = ""
    private var typeFilterNames: Set<String>? = null
    private var favoritesOnly: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerPokemon)
        val textError = findViewById<TextView>(R.id.textError)
        val searchInput = findViewById<EditText>(R.id.searchInput)
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)
        val checkFavorites = findViewById<CheckBox>(R.id.checkFavorites)

        recyclerView.layoutManager = LinearLayoutManager(this)
        // Le callback recalcule les filtres quand on (dé)favorise
        adapter = PokemonAdapter(emptyList()) { applyFilters() }
        recyclerView.adapter = adapter

        // Recherche par nom
        searchInput.doOnTextChanged { text, _, _, _ ->
            currentQuery = text.toString().lowercase()
            applyFilters()
        }

        // Case "Favoris seulement"
        checkFavorites.setOnCheckedChangeListener { _, isChecked ->
            favoritesOnly = isChecked
            applyFilters()
        }

        // Filtre par type
        val typeDisplay = listOf("Tous", "Feu", "Eau", "Plante", "Électrik", "Poison", "Vol", "Insecte", "Normal")
        val typeApi = listOf<String?>(null, "fire", "water", "grass", "electric", "poison", "flying", "bug", "normal")

        spinnerType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, typeDisplay)
        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val apiName = typeApi[position]
                if (apiName == null) {
                    typeFilterNames = null
                    applyFilters()
                } else {
                    lifecycleScope.launch {
                        try {
                            val typeResponse = RetrofitClient.service.getPokemonByType(apiName)
                            typeFilterNames = typeResponse.pokemon.map { it.pokemon.name }.toSet()
                            applyFilters()
                        } catch (e: Exception) {
                            Log.e("POKEDEX", "Erreur filtre type : ${e.message}")
                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Chargement initial
        lifecycleScope.launch {
            try {
                Log.d("POKEDEX", "Appel de l'API en cours...")
                val response = RetrofitClient.service.getPokemonList()
                Log.d("POKEDEX", "Liste reçue : ${response.results.size} pokémon")

                fullList = response.results
                applyFilters()
                recyclerView.visibility = View.VISIBLE
                textError.visibility = View.GONE
            } catch (e: Exception) {
                Log.e("POKEDEX", "Erreur réseau : ${e.message}")
                recyclerView.visibility = View.GONE
                textError.visibility = View.VISIBLE
            }
        }
    }

    // Applique recherche + type + favoris ensemble
    private fun applyFilters() {
        var result = fullList

        if (favoritesOnly) {
            result = result.filter { FavoritesManager.isFavorite(this, it.name) }
        }
        val typeSet = typeFilterNames
        if (typeSet != null) {
            result = result.filter { it.name in typeSet }
        }
        if (currentQuery.isNotEmpty()) {
            result = result.filter { it.name.lowercase().contains(currentQuery) }
        }
        adapter.updateList(result)
    }
}