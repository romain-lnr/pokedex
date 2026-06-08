package com.example.pokedex

data class PokemonListResponse(
    val results: List<PokemonEntry>
)

data class PokemonEntry(
    val name: String,
    val url: String
) {
    val id: String
        get() = url.trimEnd('/').substringAfterLast('/')

    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}

// --- Détail d'un Pokémon ---

data class PokemonDetail(
    val name: String,
    val types: List<TypeSlot>,
    val stats: List<StatSlot>
)

data class TypeSlot(
    val type: TypeInfo
)

data class TypeInfo(
    val name: String
)

data class StatSlot(
    val base_stat: Int,
    val stat: StatInfo
)

data class StatInfo(
    val name: String
)

// --- Filtre par type ---

data class TypeResponse(
    val pokemon: List<TypePokemonSlot>
)

data class TypePokemonSlot(
    val pokemon: PokemonEntry
)