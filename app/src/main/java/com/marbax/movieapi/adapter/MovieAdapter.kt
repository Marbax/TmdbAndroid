package com.marbax.movieapi.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.marbax.movieapi.R
import com.marbax.movieapi.model.FilterParams
import com.marbax.movieapi.model.Movie
import com.marbax.movieapi.movie_list.MovieListActivity
import com.marbax.movieapi.network.ApiClient
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MovieAdapter(private var movies: List<Movie>, private val activity: MovieListActivity) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>(), Filterable {

    private lateinit var params: FilterParams

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivMoviePoster: ImageView = itemView.findViewById(R.id.ivMoviePoster)
        private val tvMovieTitle: TextView = itemView.findViewById(R.id.tvMovieTitle)
        private val tvMovieRating: TextView = itemView.findViewById(R.id.tvMovieRating)
        private val tvMovieReleaseDate: TextView = itemView.findViewById(R.id.tvMovieReleaseDate)
        private val pbImgDownloadingBar: ProgressBar =
            itemView.findViewById(R.id.pbImgDownloadingBar)

        fun bindMovie(movie: Movie, activity: MovieListActivity) {
            itemView.setOnClickListener { activity.onClick(movie.id) }
            tvMovieTitle.text = movie.title
            tvMovieRating.text = movie.vote_average.toString()
            tvMovieReleaseDate.text = movie.release_date

            Glide.with(itemView.context)
                .load(ApiClient.API_IMAGE_URL + movie.poster_path)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbImgDownloadingBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbImgDownloadingBar.visibility = View.GONE
                        return false
                    }
                })
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_poster_placeholder)
                        .error(R.drawable.ic_poster_placeholder)
                )
                .into(ivMoviePoster)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_card, parent, false)
        )
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bindMovie(movie, this.activity)
    }

    fun setFilterParams(params: FilterParams) {
        this.params = params
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults: List<Movie?> =
                    if (params.dateFrom.isNotEmpty() && params.dateTo.isEmpty()) {
                        movies
                    } else {
                        getFilteredResults()
                    }
                val results = FilterResults()
                results.values = filteredResults
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                movies = results?.values as List<Movie>
                notifyDataSetChanged()
            }

        }
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getFilteredResults(): List<Movie> {
        val results: MutableList<Movie> = ArrayList()
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        val minDate = format.parse(params.dateFrom)
        val maxDate = format.parse(params.dateTo)

        for (item in movies) {
            if (item.release_date.isNotEmpty()) {
                val releaseDate = format.parse(item.release_date)
                if (releaseDate.after(minDate) && releaseDate.before(maxDate))
                    results.add(item)
            }
        }
        return results
    }
}