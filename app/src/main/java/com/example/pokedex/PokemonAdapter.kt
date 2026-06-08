package com.example.pokedex

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokemonAdapter(
    private var pokemonList: List<PokemonEntry>,
    private val onFavoriteToggled: () -> Unit = {}
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    fun updateList(newList: List<PokemonEntry>) {
        pokemonList = newList
        notifyDataSetChanged()
    }

    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.textPokemonName)
        val imagePokemon: ImageView = view.findViewById(R.id.imagePokemon)
        val imageFavorite: ImageView = view.findViewById(R.id.imageFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        val context = holder.itemView.context

        holder.textName.text = pokemon.name.replaceFirstChar { it.uppercase() }

        Glide.with(context)
            .load(pokemon.imageUrl)
            .into(holder.imagePokemon)

        // Affiche l'étoile pleine ou vide selon l'état favori
        fun refreshStar() {
            val isFav = FavoritesManager.isFavorite(context, pokemon.name)
            holder.imageFavorite.setImageResource(
                if (isFav) android.R.drawable.btn_star_big_on
                else android.R.drawable.btn_star_big_off
            )
        }
        refreshStar()

        holder.imageFavorite.setOnClickListener {
            FavoritesManager.toggle(context, pokemon.name)
            refreshStar()
            onFavoriteToggled()
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("POKEMON_NAME", pokemon.name)
            intent.putExtra("POKEMON_ID", pokemon.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = pokemonList.size
}