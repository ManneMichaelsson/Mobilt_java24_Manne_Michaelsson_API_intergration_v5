package com.example.moviedatabaseapi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

// I MovieDetailFragment.kt
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var directorTextView: TextView
    private lateinit var actorsTextView: TextView
    private lateinit var releaseDateTextView: TextView
    private lateinit var voteCountTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hämta referenser till dina TextViews
        titleTextView = view.findViewById(R.id.detail_movie_title)
        descriptionTextView = view.findViewById(R.id.detail_movie_description)
        scoreTextView = view.findViewById(R.id.detail_movie_score)
        directorTextView = view.findViewById(R.id.detail_movie_director)
        actorsTextView = view.findViewById(R.id.detail_movie_actors)
        releaseDateTextView = view.findViewById(R.id.detail_movie_release_date)
        voteCountTextView = view.findViewById(R.id.detail_movie_vote_count)

        // Hämta film-ID:t från bundlen
        val movieId = arguments?.getInt("movie_id")

        // Se till att vi faktiskt fick ett ID
        if (movieId != null) {
            titleTextView.text = "Laddar film..."

            //hämtar info om filmen
            fetchMovieDetailsById(movieId)
        }
    }

    //Hämtar specifik film från api:et
    private fun fetchMovieDetailsById(movieId: Int) {
        val apiKey = getString(R.string.API_KEY)
        val url = "https://api.themoviedb.org/3/movie/$movieId?api_key=$apiKey"

        val queue = Volley.newRequestQueue(requireContext())

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("API_SUCCESS", "Svar mottaget för URL: $url")
                try {
                    val movie = Movie(
                        id = response.getInt("id"),
                        title = response.getString("title"),
                        description = response.getString("overview"),
                        posterPath = response.getString("poster_path"),
                        voteAverage = response.getDouble("vote_average"),
                        voteCount = response.getInt("vote_count"),
                        releaseDate = response.getString("release_date")
                    )
                    updateUiWithMovieDetails(movie)
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

    //uppdaterar UI med nya filmen
    private fun updateUiWithMovieDetails(movie: Movie) {
        titleTextView.text = movie.title
        descriptionTextView.text = movie.description
        scoreTextView.text = "Score: ${movie.voteAverage}"
        releaseDateTextView.text = "Släppningsdatum: ${movie.releaseDate}"
        voteCountTextView.text = "Votecount: ${movie.voteCount}"
    }
}