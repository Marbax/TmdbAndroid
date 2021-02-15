package com.marbax.movieapi.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Singleton
object ApiClient {
    private const val URL = "https://api.themoviedb.org/3/"
    const val API_KEY = "db1ff3baf003eb28c227e02bda6b00b2"
    const val API_IMAGE_URL = "https://image.tmdb.org/t/p/w500"
    const val API_SORT_BY = "release_date.desc"

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create()) // Moshi - analog
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}