package com.example.pokemonapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class PokemonAdapter(
    private val pokemonList: MutableList<Pokemon>,
    private val onClick: (String) -> Unit // Click listener for item clicks
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
        Glide.with(viewHolder.itemView.context).load(pokemon.url).into(viewHolder.imageView)

        // Set click listener to navigate to details
        viewHolder.itemView.setOnClickListener {
            onClick(pokemon.url) // Use the click listener passed to the adapter
        }
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    fun addPokemons(newPokemons: List<Pokemon>) {
        val startPosition = pokemonList.size
        pokemonList.addAll(newPokemons)
        notifyItemRangeInserted(startPosition, newPokemons.size)
    }

    fun updatePokemons(newPokemons: List<Pokemon>) {
        pokemonList.clear() // Clear the existing list
        pokemonList.addAll(newPokemons) // Add the new list
        notifyDataSetChanged() // Notify the adapter to refresh the view
    }
}