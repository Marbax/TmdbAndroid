package com.marbax.movieapi.movie_details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.AppBarLayout
import com.marbax.movieapi.Const.KEY_MOVIE_ID
import com.marbax.movieapi.R
import com.marbax.movieapi.adapter.CastAdapter
import com.marbax.movieapi.model.Cast
import com.marbax.movieapi.model.Movie
import com.marbax.movieapi.movie_list.MovieListActivity
import com.marbax.movieapi.network.ApiClient
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import kotlinx.android.synthetic.main.movie_card.*

class DetailsActivity : AppCompatActivity(), DetailsContract.View {

    private val presenter = MoviePresenter(this)

    private var movieTitle = ""
    private lateinit var castAdapter: CastAdapter
    private val castList = mutableListOf<Cast>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        initToolbar()
        val id = intent.getIntExtra(KEY_MOVIE_ID, -1)
        castAdapter = CastAdapter(castList)
        rv_cast.adapter = castAdapter
        presenter.getMovie(id)
    }

    private fun initToolbar() {
        ctlMovieDetails.title = ""
        ablMovieDetails.setExpanded(true)
        ablMovieDetails.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {

            var isShown = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange ==-1) {
                    appBarLayout?.totalScrollRange?.apply {
                        scrollRange = appBarLayout.totalScrollRange
                    }
                }

                if (scrollRange + verticalOffset == 0) {
                    ctlMovieDetails.title = movieTitle
                    isShown = true
                } else if (isShown)
                    ctlMovieDetails.title = ""
                isShown = false
            }
        })

    }

    private fun initPoster(url: String) {
        Glide.with(this)
            .load(ApiClient.API_IMAGE_URL + url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    pbMovieDetailsBackdropLoading.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    pbMovieDetailsBackdropLoading.visibility = View.GONE
                    return false
                }
            })
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_poster_placeholder)
                    .error(R.drawable.ic_poster_placeholder)
            )
            .into(ctlImageBackdrop)
    }

    override fun showProgress() {
        pbCastsLoading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        pbCastsLoading.visibility = View.GONE
    }

    override fun showMovieDetails(movie: Movie) {
        movieTitle = movie.title
        ctlImageBackdropTitle.text = movieTitle
        tv_release_date.text = movie.release_date
        tv_movie_ratings.text = movie.vote_average.toString()
        tv_movie_overview.text = movie.overview
        tv_homepage_value.text = movie.homepage
        tv_tagline_value.text = movie.tagline
        tv_runtime_value.text = movie.runtime
        initPoster(movie.poster_path)
        castList.clear()
        castList.addAll(movie.credits.cast)
        castAdapter.notifyDataSetChanged()
    }

    override fun showError(t: Throwable) {
        Toast.makeText(this, "Error! ${t.message}", Toast.LENGTH_SHORT).show()
        Log.e(MovieListActivity::class.java.simpleName, t.message.toString())
    }
}