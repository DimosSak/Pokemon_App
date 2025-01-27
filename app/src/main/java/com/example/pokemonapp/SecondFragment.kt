package com.example.pokemonapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText // Import for EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
class SecondFragment : Fragment() {
    private lateinit var viewModel: PokemonViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadMoreButton: Button
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var nameEditText: EditText
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchResultsAdapter: PokemonAdapter // New adapter for search results

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        recyclerView = view.findViewById(R.id.pokemon_recycler_view)
        loadMoreButton = view.findViewById(R.id.load_more_button)
        nameEditText = view.findViewById(R.id.name_edit_text)
        searchResultsRecyclerView = view.findViewById(R.id.search_results_recycler_view)

        setupRecyclerView()
        setupSearchResultsRecyclerView()

        viewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)

        val selectedType = arguments?.getString("type") ?: ""
        observeViewModel()
        viewModel.fetchPokemonsByType(selectedType)

        loadMoreButton.setOnClickListener {
            viewModel.loadMorePokemons()
        }

        // Set up the search functionality
        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString().trim()
                if (searchText.isNotEmpty()) {
                    // Show the search results RecyclerView
                    searchResultsRecyclerView.visibility = View.VISIBLE
                    viewModel.filterPokemonsByName(searchText) // Call the filter method
                } else {
                    // Hide the search results RecyclerView if the search text is empty
                    searchResultsRecyclerView.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        // Clear the search results when returning to this fragment
        viewModel.clearSearchResults()
        searchResultsRecyclerView.visibility = View.GONE // Hide the search results RecyclerView
        nameEditText.text?.clear() // Clear the search EditText
    }

    private fun setupRecyclerView() {
        pokemonAdapter = PokemonAdapter(mutableListOf()) { pokemonUrl ->
            // Handle item click to navigate to detail
            val action = SecondFragmentDirections.actionSecondFragmentToPokemonDetailFragment(pokemonUrl)
            findNavController().navigate(action) // Use findNavController() to navigate
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = pokemonAdapter
    }

    private fun setupSearchResultsRecyclerView() {
        searchResultsAdapter = PokemonAdapter(mutableListOf()) { pokemonUrl ->
            // Handle item click to navigate to detail
            val action = SecondFragmentDirections.actionSecondFragmentToPokemonDetailFragment(pokemonUrl)
            findNavController().navigate(action) // Use findNavController() to navigate
        }
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(context)
        searchResultsRecyclerView.adapter = searchResultsAdapter
    }

    private fun observeViewModel() {
        viewModel.displayedPokemons.observe(viewLifecycleOwner, { pokemons ->
            if (pokemons.isEmpty()) {
                Toast.makeText(context, "No Pokémon found", Toast.LENGTH_SHORT).show()
            } else {
                pokemonAdapter.updatePokemons(pokemons) // Update the adapter with the displayed Pokémon
            }
        })

        viewModel.searchResults.observe(viewLifecycleOwner, { searchResults ->
            if (searchResults.isEmpty()) {
                searchResultsRecyclerView.visibility = View.GONE // Hide if no results
            } else {
                searchResultsRecyclerView.visibility = View.VISIBLE // Show search results
                searchResultsAdapter.updatePokemons(searchResults) // Update the search results adapter
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, { message ->
            message?.let {
                showError(it)
            }
        })
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}