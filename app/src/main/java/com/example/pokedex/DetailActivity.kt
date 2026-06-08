package com.example.pokedex

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detailRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val name = intent.getStringExtra("POKEMON_NAME") ?: "Inconnu"
        val id = intent.getStringExtra("POKEMON_ID") ?: ""
        val imageUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

        val textName = findViewById<TextView>(R.id.textDetailName)
        val imageDetail = findViewById<ImageView>(R.id.imageDetail)
        val textTypes = findViewById<TextView>(R.id.textTypes)
        val textStats = findViewById<TextView>(R.id.textStats)
        val buttonBack = findViewById<Button>(R.id.buttonBack)

        textName.text = name.replaceFirstChar { it.uppercase() }
        Glide.with(this).load(imageUrl).into(imageDetail)

        // Bouton retour vers la liste
        buttonBack.setOnClickListener {
            finish()
        }

        lifecycleScope.launch {
            try {
                val detail = RetrofitClient.service.getPokemonDetail(id)

                val types = detail.types.joinToString(", ") {
                    it.type.name.replaceFirstChar { c -> c.uppercase() }
                }
                textTypes.text = "Type(s) : $types"

                val hp = detail.stats.find { it.stat.name == "hp" }?.base_stat ?: 0
                val attack = detail.stats.find { it.stat.name == "attack" }?.base_stat ?: 0
                textStats.text = "HP : $hp     Attaque : $attack"

            } catch (e: Exception) {
                textTypes.text = "Impossible de charger les détails"
                Log.e("POKEDEX", "Erreur détail: ${e.message}")
            }
        }
    }
}