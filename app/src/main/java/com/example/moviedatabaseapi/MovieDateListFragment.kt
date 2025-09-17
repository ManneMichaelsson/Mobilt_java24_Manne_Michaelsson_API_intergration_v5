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
class MovieDateListFragment : Fragment(R.layout.fragment_movie_date_list) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private val movieList = mutableListOf<Movie>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hämta referenser till dina vyer
        recyclerView = view.findViewById(R.id.recyclerView)
        val titleText: TextView = view.findViewById(R.id.textViewWelcome) // Antag att ID:t är detta

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        movieAdapter = MovieAdapter(movieList, R.id.action_movieDateListFragment_to_movieDetailFragment3)
        recyclerView.adapter = movieAdapter

        // Hämta datumet från bundlen
        val releaseDate = arguments?.getString("release_date")

        // Kontrollera om vi fick ett giltigt datum
        if (!releaseDate.isNullOrBlank()) {
            // Om ja, uppdatera rubriken och hämta filmer för det datumet
            titleText.text = "Filmer släppta: $releaseDate"

            val apiKey = getString(R.string.API_KEY)
            val url = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&primary_release_date.gte=$releaseDate&primary_release_date.lte=$releaseDate"
            fetchDataFromApi(url)
        } else {
            // Om nej, visa populära filmer som ett standardfall
            titleText.text = "Populära filmer"
            fetchMovies()
        }
    }

    //Hämta data som sedan startar updateMovies i movieAdapter som gör om listan med nya filmer i fragment.
    private fun fetchDataFromApi(url: String) {
        //skapar en request kö
        val queue = Volley.newRequestQueue(requireContext())

        //skapar ett objekt och kör GET metod på url från stringen som är som attibut. Skapar ett objekt av Movie vid varje resultat
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("API_SUCCESS", "Svar mottaget för URL: $url")
                try {
                    //Skapar array med resultatet
                    val moviesArray = response.getJSONArray("results")

                    //Skapar lista med objekt av Movies
                    val parsedMovies = mutableListOf<Movie>()

                    //För varje film i resultatet så skapas det ett objekt movie och hämtar info från nycklarna i API.
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
                        //lägger till movie objektet i listan parsedMovies
                        parsedMovies.add(movie)
                    }
                    movieAdapter.updateMovies(parsedMovies)

                } catch (e: JSONException) {
                    Log.e("API_ERROR", "Fel vid JSON-bearbetning: ${e.message}")
                }
            },
            { error ->
                Log.e("API_ERROR", "Volley-fel för URL $url: ${error.message}")
            }
        )
        queue.add(jsonObjectRequest)
    }

    //hämtar api nyckel och länken och startar fetchDataFromApi med url som atribut
    private fun fetchMovies() {
        val apiKey = getString(R.string.API_KEY)
        val url = "https://api.themoviedb.org/3/movie/popular?api_key=$apiKey"
        // Anropa den gemensamma metoden
        fetchDataFromApi(url)
    }
}