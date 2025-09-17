package com.example.moviedatabaseapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MovieAdapter(private var movieList: MutableList<Movie>, private val onMovieClickActionId: Int)
    : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Hämtar alla views
        val posterImage: ImageView = itemView.findViewById(R.id.moviePosterImageView)
        val titleText: TextView = itemView.findViewById(R.id.movieTitleTextView)
        val scoreText: TextView = itemView.findViewById(R.id.movieScoreTextView)
        val descriptionText: TextView = itemView.findViewById(R.id.textViewDescription)
        val releaseDateText: TextView = itemView.findViewById(R.id.detail_movie_release_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_list_item, parent, false)
        return MovieViewHolder(view)
    }

    // Metod för att hämta storleken på en lista
    override fun getItemCount(): Int {
        return movieList.size
    }

    // Metod som styr viewen MovieView (själva filmviewn)
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]

        //hämtar varje films views
        holder.titleText.text = movie.title
        holder.descriptionText.text = movie.description
        holder.scoreText.text = "Betyg: ${movie.voteAverage}"
        holder.posterImage.setImageResource(R.drawable.ic_launcher_background)

        val baseUrl = "https://image.tmdb.org/t/p/w500"
        val fullPosterUrl = baseUrl + movie.posterPath

        // Använder Glide för att ladda bilden
        Glide.with(holder.itemView.context)
            .load(fullPosterUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.posterImage) //sätter in bilden


        holder.releaseDateText.text = "Släppningsdatum: ${movie.releaseDate}"

        // Lyssnare 1: För att klicka på datumet
        holder.releaseDateText.setOnClickListener {
            val clickedDate = movie.releaseDate
            val bundle = Bundle()
            bundle.putString("release_date", clickedDate)

            holder.itemView.findNavController().navigate(
                R.id.action_movieListFragment_to_movieDateListFragment, // Korrekt action från MovieList
                bundle
            )
        }

        // Lyssnare 2: För att klicka på hela raden
        holder.itemView.setOnClickListener {
            val clickedMovieId = movie.id
            val bundle = Bundle()
            bundle.putInt("movie_id", clickedMovieId)

            // Använd det action-ID som skickades med till adaptern!
            holder.itemView.findNavController().navigate(onMovieClickActionId, bundle)
        }
    }
    fun updateMovies(newMovies: List<Movie>) {
        movieList.clear()           // 1. Rensa den gamla listan
        movieList.addAll(newMovies)   // 2. Lägg till alla nya filmer
        notifyDataSetChanged()      // 3. Säg till RecyclerView att rita om allt!
    }
}