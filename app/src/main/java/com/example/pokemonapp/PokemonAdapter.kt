package com.example.pokemonapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonAdapter(
    private val pokemonList: MutableList<Pokemon>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.pokemon_name)
        val imageView: ImageView = itemView.findViewById(R.id.pokemon_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        viewHolder.nameTextView.text = pokemon.name

        // Fetch Pokémon details to get the image
        ApiClient.instance.getPokemonDetails(pokemon.url).enqueue(object : Callback<PokemonDetail> {
            override fun onResponse(call: Call<PokemonDetail>, response: Response<PokemonDetail>) {
                if (response.isSuccessful) {
                    val imageUrl = response.body()?.sprites?.front_default
                    Glide.with(viewHolder.itemView.context)
                        .load(imageUrl) // Load the image into the ImageView
                        .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image
                        .into(viewHolder.imageView)
                }
            }

            override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                // Handle failure (optional: show a placeholder or log the error)
            }
        })

        // Handle click events
        viewHolder.itemView.setOnClickListener {
            onClick(pokemon.url)
        }
    }

    override fun getItemCount(): Int = pokemonList.size

    fun addPokemons(newPokemons: List<Pokemon>) {
        val startPosition = pokemonList.size
        pokemonList.addAll(newPokemons)
        notifyItemRangeInserted(startPosition, newPokemons.size)
    }

    fun updatePokemons(newPokemons: List<Pokemon>) {
        pokemonList.clear()
        pokemonList.addAll(newPokemons)
        notifyDataSetChanged()
    }
}
