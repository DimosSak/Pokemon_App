package com.example.pokemonapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import androidx.navigation.fragment.navArgs


class PokemonDetailFragment : Fragment() {
    private lateinit var viewModel: PokemonDetailViewModel
    private lateinit var nameTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var hpTextView: TextView
    private lateinit var attackTextView: TextView
    private lateinit var defenseTextView: TextView
    private lateinit var imagePlaceholder: TextView // New TextView for the placeholder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pokemon_detail, container, false)

        nameTextView = view.findViewById(R.id.pokemon_name)
        imageView = view.findViewById(R.id.pokemon_image)
        hpTextView = view.findViewById(R.id.pokemon_hp)
        attackTextView = view.findViewById(R.id.pokemon_attack)
        defenseTextView = view.findViewById(R.id.pokemon_defense)
        imagePlaceholder = view.findViewById(R.id.image_placeholder) // Initialize the placeholder TextView

        // Retrieve the Pokémon URL from Safe Args
        val args: PokemonDetailFragmentArgs by navArgs()
        val pokemonUrl = args.pokemonUrl
        viewModel = ViewModelProvider(this).get(PokemonDetailViewModel::class.java)
        Log.d("DetailFragment", " URL: $pokemonUrl")
        viewModel.fetchPokemonDetail(pokemonUrl)

        observeViewModel()

        return view
    }

    private fun observeViewModel() {
        viewModel.pokemonDetail.observe(viewLifecycleOwner, { detail ->
            if (detail != null) {
                nameTextView.text = detail.name

                // Check if the image URL is null
                if (detail.sprites.front_default != null) {
                    Glide.with(this).load(detail.sprites.front_default).into(imageView)
                    imagePlaceholder.visibility = View.GONE // Hide placeholder if image is available
                } else {
                    imageView.setImageDrawable(null) // Clear the image view
                    imagePlaceholder.visibility = View.VISIBLE // Show placeholder message
                }

                // Log the stats to see if they are populated
                Log.d("PokemonDetailFragment", "Stats: ${detail.stats}")

                val hpStat = detail.stats.find { it.stat.name == "hp" }
                val attackStat = detail.stats.find { it.stat.name == "attack" }
                val defenseStat = detail.stats.find { it.stat.name == "defense" }

                // Log the values before setting them
                Log.d("PokemonDetailFragment", "Setting HP: ${hpStat?.base_stat}, Attack: ${attackStat?.base_stat}, Defense: ${defenseStat?.base_stat}")

                hpTextView.text = "HP: ${hpStat?.base_stat ?: "N/A"}"
                attackTextView.text = "Attack: ${attackStat?.base_stat ?: "N/A"}"
                defenseTextView.text = "Defense: ${defenseStat?.base_stat ?: "N/A"}"
            } else {
                showError("No details available")
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, { message ->
            message?.let {
                showError(it)
            }
        })
    }

    private fun showError(message: String) {
        // Show a Toast message for error handling
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}