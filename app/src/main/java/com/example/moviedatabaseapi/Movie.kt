package com.example.moviedatabaseapi

data class Movie (
    val id: Int,
    val title: String,
    val description: String,
    val posterPath: String,
    val voteAverage: Double,
    val voteCount: Int,
    val releaseDate: String
)