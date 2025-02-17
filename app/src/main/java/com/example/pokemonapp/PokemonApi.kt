package com.example.pokemonapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface PokemonApi {
    @GET("type/{type}")
    fun getPokemonsByType(
        @Path("type") type: String,
    ): Call<PokemonResponse>

    @GET("pokemon/{id}")
    fun getPokemonDetail(@Path("id") id: Int): Call<PokemonDetail>
    @GET
    fun getPokemonDetails(@Url pokemonUrl: String): Call<PokemonDetail> // Fetch Pokémon details

}