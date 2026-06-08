package com.example.pokedex

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("api/v2/pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int = 100): PokemonListResponse

    @GET("api/v2/pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: String): PokemonDetail

    @GET("api/v2/type/{name}")
    suspend fun getPokemonByType(@Path("name") name: String): TypeResponse
}