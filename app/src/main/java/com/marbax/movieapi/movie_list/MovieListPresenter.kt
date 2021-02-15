package com.marbax.movieapi.movie_list

import com.marbax.movieapi.model.Movie

class MovieListPresenter(private val movieListView: MovieListContract.View) :
    MovieListContract.Presenter, MovieListContract.Model.OnFinishedListener {

    private val movieListModel = MovieListModel()

    override fun getMovies() {
        movieListView.showProgress()
        movieListModel.getMovieList(this)
    }

    override fun getMoviesByDateRange(dateFrom: String, dateTo: String) {
        movieListView.showProgress()
        movieListModel.getMovieListByDateRange(this, dateFrom, dateTo)
    }

    override fun onSuccess(movies: List<Movie>?) {
        movieListView.submitMovies(movies)
        movieListView.hideProgress()
    }

    override fun onFailure(t: Throwable) {
        movieListView.showError(t)
        movieListView.hideProgress()
    }
}