package com.example.pokemonapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class PokemonDetailViewModel : ViewModel() {
    private val _pokemonDetail = MutableLiveData<PokemonDetail>()
    val pokemonDetail: LiveData<PokemonDetail> get() = _pokemonDetail

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    fun extractNumberFromUrl(url: String): String? {
        // Define a regex pattern to extract the number before the last slash
        val regex = ".*/(\\d+)/$".toRegex()
        // Match the URL against the regex pattern
        val matchResult = regex.find(url)
        // Return the captured group (the number) or null if no match is found
        return matchResult?.groups?.get(1)?.value
    }

    fun fetchPokemonDetail(url: String) {
        // Extract the Pokémon ID from the URL
        val idString = extractNumberFromUrl(url)
        Log.d("fetchPokemonDetail", "we have: $idString")
        if (!idString.isNullOrEmpty()) {
            val pokemonId = idString.toInt()
            Log.d("PokemonDetailViewModel", "Fetching details for Pokémon ID: $pokemonId")

            // Call the API with the extracted ID
            ApiClient.instance.getPokemonDetail(pokemonId).enqueue(object : Callback<PokemonDetail> {
                override fun onResponse(call: Call<PokemonDetail>, response: Response<PokemonDetail>) {
                    if (response.isSuccessful) {
                        _pokemonDetail.value = response.body()
                        Log.d("PokemonDetailViewModel", "Fetched Pokémon details: ${response.body()}")
                        // Log the entire response body to see its structure
                        Log.d("PokemonDetailViewModel", "Response Body: ${response.body()}")
                    } else {
                        _errorMessage.value = "Failed to load Pokémon details"
                        Log.e("PokemonDetailViewModel", "Error response: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                    _errorMessage.value = "Network error: ${t.message}"
                    Log.e("PokemonDetailViewModel", "Network error: ${t.message}")
                }
            })
        } else {
            _errorMessage.value = "Invalid Pokémon URL"
            Log.e("PokemonDetailViewModel", "Invalid Pokémon URL")
        }
    }
    fun fetchPokemonDetailByName(name: String) {
        ApiClient.instance.getPokemonDetailByName(name).enqueue(object : Callback<PokemonDetail> {
            override fun onResponse(call: Call<PokemonDetail>, response: Response<PokemonDetail>) {
                if (response.isSuccessful) {
                    _pokemonDetail.value = response.body()
                    Log.d("PokemonDetailViewModel", "Fetched Pokémon details: ${response.body()}")
                } else {
                    _errorMessage.value = "Failed to load Pokémon details"
                    Log.e("PokemonDetailViewModel", "Error response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                _errorMessage.value = "Network error: ${t.message}"
                Log.e("PokemonDetailViewModel", "Network error: ${t.message}")
            }
        })
    }

}