package com.example.pokemonapp

data class Pokemon(
    val name: String,
    val url: String
)

data class PokemonResponse(
    val id: Int,
    val name: String,
    val pokemon: List<PokemonEntry> // Change this to List<PokemonEntry>
)

data class PokemonEntry(
    val slot: Int,
    val pokemon: Pokemon // This represents the Pokémon object
)

data class PokemonDetail(
    val name: String,
    val sprites: Sprites,
    val stats: List<Stat>
)

data class Sprites(
    val front_default: String
)

data class Stat(
    val base_stat: Int,
    val stat: StatInfo
)

data class StatInfo(
    val name: String
)