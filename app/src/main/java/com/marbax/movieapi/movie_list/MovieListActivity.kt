package com.marbax.movieapi.movie_list

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.marbax.movieapi.Const.KEY_DATE_FROM
import com.marbax.movieapi.Const.KEY_DATE_TO
import com.marbax.movieapi.Const.KEY_MOVIE_ID
import com.marbax.movieapi.Const.REQUEST_FILTER_CODE
import com.marbax.movieapi.Const.RESET_FILTER_CODE
import com.marbax.movieapi.R
import com.marbax.movieapi.adapter.MovieAdapter
import com.marbax.movieapi.filter_activity.FilterActivity
import com.marbax.movieapi.model.Movie
import com.marbax.movieapi.movie_details.DetailsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MovieListActivity : AppCompatActivity(), MovieListContract.View, ClickListener {

    private var adapter: MovieAdapter? = null
    private val presenter = MovieListPresenter(this)
    private val moviesList = mutableListOf<Movie>()
    private var dateFrom = ""
    private var dateTo = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.getMovies()
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = MovieAdapter(moviesList, this)
        recyclerView.adapter = adapter
        fabMovieFilter.setOnClickListener {
            val intent = Intent(this, FilterActivity::class.java)
            intent.putExtra(KEY_DATE_FROM, dateFrom)
            intent.putExtra(KEY_DATE_TO, dateTo)
            startActivityForResult(intent, REQUEST_FILTER_CODE)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
            recyclerView.layoutManager = GridLayoutManager(this, 2)
        else
            recyclerView.layoutManager = GridLayoutManager(this, 3)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FILTER_CODE && resultCode == Activity.RESULT_OK) {
            data?.apply {
                dateFrom = this.getStringExtra(KEY_DATE_FROM)!!
                dateTo = this.getStringExtra(KEY_DATE_TO)!!
            }
            adapter?.apply {
                presenter.getMoviesByDateRange(dateFrom, dateTo)
                //this.setFilterParams(FilterParams(dateFrom, dateTo))
                //this.filter.filter("")
            }
        } else if (requestCode == REQUEST_FILTER_CODE && resultCode == RESET_FILTER_CODE) {
            data?.apply {
                dateFrom = this.getStringExtra(KEY_DATE_FROM)!!
                dateTo = this.getStringExtra(KEY_DATE_TO)!!
            }
            adapter?.apply {
                presenter.getMovies()
                //this.setFilterParams(FilterParams(dateFrom, dateTo))
            }
        }

    }

    override fun showProgress() {
        pbViewLoading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        pbViewLoading.visibility = View.GONE
    }

    override fun submitMovies(movies: List<Movie>?) {
        movies?.apply {
            moviesList.clear()
            moviesList.addAll(this)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun showError(t: Throwable) {
        Toast.makeText(this, "Error! ${t.message}", Toast.LENGTH_SHORT).show()
        Log.e(MovieListActivity::class.java.simpleName, t.message.toString())
    }

    override fun onClick(id: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
            .putExtra(KEY_MOVIE_ID, id)
        startActivity(intent)
    }
}