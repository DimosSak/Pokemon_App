package com.example.pokemonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController

class FirstFragment : Fragment() {
    private lateinit var typeSpinner: Spinner
    private lateinit var searchButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        typeSpinner = view.findViewById(R.id.type_spinner)
        searchButton = view.findViewById(R.id.search_button)

        val types = arrayOf("fire", "water", "grass", "electric", "dragon", "psychic", "ghost", "dark", "steel", "fairy")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        typeSpinner.adapter = adapter

        searchButton.setOnClickListener {
            val selectedType = typeSpinner.selectedItem.toString()
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(selectedType)
            findNavController().navigate(action)
        }

        return view
    }
}