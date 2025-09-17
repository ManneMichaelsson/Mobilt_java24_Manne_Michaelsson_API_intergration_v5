package com.example.moviedatabaseapi
// I StartFragment.kt
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class StartFragment : Fragment(R.layout.fragment_start) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Hämtar knappar på startsidan
        val searchButton: Button = view.findViewById(R.id.buttonSearch)
        val popularButton: Button = view.findViewById(R.id.buttonShowPopular)
        val searchEditText: EditText = view.findViewById(R.id.editTextSearch)

        //lyssnar på "show popular" knappen
        popularButton.setOnClickListener {
            // Skickade vidare till nytt fragment om knappen klickas på
            findNavController().navigate(R.id.action_startFragment_to_movieListFragment)
        }

        //Lyssnar på "Search" knappen
        searchButton.setOnClickListener {
            // Hämta texten från EditText-fältet
            val searchQuery = searchEditText.text.toString()

            if (searchQuery.isEmpty()) {
                //om sökord är tomt så går det inte
                Toast.makeText(requireContext(), "Sökord kan inte vara tomt", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                // Skapar en bundle för det användare har skrivit in som sökord till nästa fragment
                val bundle = Bundle()
                bundle.putString("search_query", searchQuery)

                findNavController().navigate(R.id.action_startFragment_to_movieListFragment, bundle)
            }
        }
    }
}