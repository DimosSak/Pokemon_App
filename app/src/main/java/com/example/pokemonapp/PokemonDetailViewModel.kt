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
    fun fetchPokemonDetail(url: String) {
        if (!url.isNullOrEmpty()) {
            ApiClient.instance.getPokemonDetails(url).enqueue(object : Callback<PokemonDetail> {
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

}