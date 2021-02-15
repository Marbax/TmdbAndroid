package com.marbax.movieapi.movie_details

import com.marbax.movieapi.model.Movie

interface DetailsContract {

    interface Model {

        interface OnFinishedListener {

            fun onSuccess(movie: Movie)
            fun onFailure(t: Throwable)
        }

        fun getMovieDetails(onFinishedListener: OnFinishedListener, id: Int)
    }

    interface View {

        fun showProgress()
        fun hideProgress()
        fun showMovieDetails(movie: Movie)
        fun showError(t: Throwable)
    }

    interface Presenter {

        fun getMovie(id:Int)
    }
}

