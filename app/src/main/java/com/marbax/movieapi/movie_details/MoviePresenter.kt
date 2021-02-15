package com.marbax.movieapi.movie_details

import com.marbax.movieapi.model.Movie

class MoviePresenter(private val detailsView: DetailsContract.View) :
    DetailsContract.Presenter, DetailsContract.Model.OnFinishedListener {

    private val model = MovieModel()


    override fun onSuccess(movie: Movie) {
        detailsView.hideProgress()
        detailsView.showMovieDetails(movie)
    }

    override fun onFailure(t: Throwable) {
        detailsView.hideProgress()
        detailsView.showError(t)
    }

    override fun getMovie(id: Int) {
        detailsView.showProgress()
        model.getMovieDetails(this, id)
    }

}