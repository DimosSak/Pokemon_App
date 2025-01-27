package com.example.pokemonapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonViewModel : ViewModel() {
    private val _allPokemons = MutableLiveData<List<Pokemon>>() // Store all fetched Pokémon
    val allPokemons: LiveData<List<Pokemon>> get() = _allPokemons

    private val _displayedPokemons = MutableLiveData<List<Pokemon>>() // Store currently displayed Pokémon
    val displayedPokemons: LiveData<List<Pokemon>> get() = _displayedPokemons

    private val _searchResults = MutableLiveData<List<Pokemon>>() // Store search results
    val searchResults: LiveData<List<Pokemon>> get() = _searchResults

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private var currentOffset = 0 // Track the current offset for loading more
    private val limit = 10 // Number of Pokémon to fetch at a time

    fun fetchPokemonsByType(type: String) {
        _loading.value = true
        ApiClient.instance.getPokemonsByType(type).enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                _loading.value = false
                if (response.isSuccessful) {
                    // Extract the Pokémon from the response
                    val newPokemons = response.body()?.pokemon?.map { it.pokemon } ?: emptyList()
                    _allPokemons.value = newPokemons // Store all Pokémon
                    currentOffset = 10 // Reset offset for the first load
                    _displayedPokemons.value = newPokemons.take(limit) // Load the first set of Pokémon to display
                } else {
                    _errorMessage.value = "Failed to load Pokémon"
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                _loading.value = false
                _errorMessage.value = "Network error: ${t.message}"
            }
        })
    }

    fun loadMorePokemons() {
        val currentList = _displayedPokemons.value ?: emptyList()
        val allPokemonsList = _allPokemons.value ?: emptyList()

        // Get the next set of Pokémon based on the current offset
        val nextPokemons = allPokemonsList.drop(currentOffset).take(limit)

        // Append new Pokémon to the displayed list
        _displayedPokemons.value = currentList + nextPokemons

        // Update the offset after loading
        currentOffset += nextPokemons.size // Increment the offset by the number of newly added Pokémon
    }
    fun clearSearchResults() {
        _searchResults.value = emptyList() // Clear the search results
    }
    fun filterPokemonsByName(name: String) {
        val filteredList = _allPokemons.value?.filter {
            it.name.contains(name, ignoreCase = true) // Filter based on name
        }?.take(3) ?: emptyList() // Limit to the first 3 results
        _searchResults.value = filteredList // Update search results
    }
}