package com.marbax.movieapi.movie_list

import com.marbax.movieapi.model.MovieResponse
import com.marbax.movieapi.network.ApiClient
import com.marbax.movieapi.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListModel : MovieListContract.Model {

    override fun getMovieList(onFinishedListener: MovieListContract.Model.OnFinishedListener) {
        val apiService = ApiClient.buildService(ApiService::class.java)
        val call = apiService.getMovies()
        call.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                val movies = response.body()?.results
                onFinishedListener.onSuccess(movies)
            }

        })
    }

    override fun getMovieListByDateRange(
        onFinishedListener: MovieListContract.Model.OnFinishedListener,
        dateFrom: String,
        dateTo: String
    ) {
        val apiService = ApiClient.buildService(ApiService::class.java)
        val call = apiService.getMoviesByDateRange(dateFrom, dateTo)

        call.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                val movies = response.body()?.results
                onFinishedListener.onSuccess(movies)
            }

        })
    }


}
