package com.marbax.movieapi.network

import com.marbax.movieapi.Const.CREDITS
import com.marbax.movieapi.model.Movie
import com.marbax.movieapi.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    fun getMovies(@Query("api_key") apiKey: String = ApiClient.API_KEY):
            Call<MovieResponse>

    @GET("discover/movie")
    fun getMoviesByDateRange(
        @Query("release_date.gte") dateFrom: String,
        @Query("release_date.lte") dateTo: String,
        @Query("sort_by") sortBy: String = ApiClient.API_SORT_BY,
        @Query("api_key") apiKey: String = ApiClient.API_KEY
    ):
            Call<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieInfo(
        @Path("movie_id")
        movieId: Int,
        @Query("api_key")
        apiKey: String = ApiClient.API_KEY,
        @Query("append_to_response")
        credits: String = CREDITS
    ): Call<Movie>
}