package com.example.moviedatabaseapi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

// Notera att vi skickar in layouten i konstruktorn, ett smidigt sätt att slippa onCreateView
class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    //Hämtar views
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private val movieList = mutableListOf<Movie>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hämtar recyclerView och sätter att den går att skrolla i
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val titleText: TextView = view.findViewById(R.id.textViewWelcome)

        // Skapa och sätt adaptern
        movieAdapter = MovieAdapter(movieList, R.id.action_movieListFragment_to_movieDetailFragment)
        recyclerView.adapter = movieAdapter

        //hämtar sökordet från senaste fragment genom en bundle
        val searchQuery = arguments?.getString("search_query")

        //kollar så den inte är blank
        if (!searchQuery.isNullOrBlank()) {
            titleText.text = "Sökresultat för: $searchQuery"
            searchMovies(searchQuery)
        } else {
            titleText.text = "Populära filmer"
            fetchMovies()
        }
    }

    private fun searchMovies(query: String?) {
        if (query.isNullOrBlank()) {
            Log.e("API_ERROR", "Sökord kan inte vara tomt")
            return
        }
        else{
            Log.d("API_SUCCESS", "Sökord: $query")
            val apiKey = getString(R.string.API_KEY)
            val url = "https://api.themoviedb.org/3/search/movie?api_key=$apiKey&query=$query"
            fetchDataFromApi(url)
        }
    }

    private fun fetchDataFromApi(url: String) {

        //skapar en request kö
        val queue = Volley.newRequestQueue(requireContext())

        val jsonObjectRequest = JsonObjectRequest(
            //skickar en GET förfrågan mot URL
            Request.Method.GET, url, null,
            { response ->
                Log.d("API_SUCCESS", "Svar mottaget för URL: $url")

                //skapar en lista med Movie objekt och fyller den med info från get förfrågan
                try {
                    val moviesArray = response.getJSONArray("results")
                    val parsedMovies = mutableListOf<Movie>()

                    for (i in 0 until moviesArray.length()) {
                        val movieObject = moviesArray.getJSONObject(i)

                        val movie = Movie(
                            id = movieObject.getInt("id"),
                            title = movieObject.getString("title"),
                            description = movieObject.getString("overview"),
                            posterPath = movieObject.getString("poster_path"),
                            voteAverage = movieObject.getDouble("vote_average"),
                            voteCount = movieObject.getInt("vote_count"),
                            releaseDate = movieObject.getString("release_date")
                        )
                        //lägger till objekt i listan
                        parsedMovies.add(movie)
                    }
                    //uppdaterar sidan med hjälp av metoden updateMovies i movieAdapter
                    movieAdapter.updateMovies(parsedMovies)

                } catch (e: JSONException) {
                    Log.e("API_ERROR", "Fel vid JSON-bearbetning: ${e.message}")
                }
            },
            { error -> // Viktigt att ha med felhantering även här!
                Log.e("API_ERROR", "Volley-fel för URL $url: ${error.message}")
            }
        )
        queue.add(jsonObjectRequest)
    }

    private fun fetchMovies() {
        val apiKey = getString(R.string.API_KEY)
        val url = "https://api.themoviedb.org/3/movie/popular?api_key=$apiKey"
        // Anropa den gemensamma metoden
        fetchDataFromApi(url)
    }
}