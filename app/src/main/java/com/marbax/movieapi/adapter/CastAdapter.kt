package com.marbax.movieapi.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.marbax.movieapi.R
import com.marbax.movieapi.model.Cast
import com.marbax.movieapi.network.ApiClient

class CastAdapter(private val casts: List<Cast>) :
    RecyclerView.Adapter<CastAdapter.CastViewHolder>() {
    class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private fun initPoster(url: String) {
            val pbPosterLoading = itemView.findViewById<ProgressBar>(R.id.pbCastLoading)
            val castPoster = itemView.findViewById<ImageView>(R.id.ivCastPoster)
            Glide.with(itemView.context)
                .load(ApiClient.API_IMAGE_URL + url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbPosterLoading.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbPosterLoading.visibility = View.GONE
                        return false
                    }
                })
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_poster_placeholder)
                        .error(R.drawable.ic_poster_placeholder)
                )
                .into(castPoster)
        }

        fun bind(cast: Cast) {
            initPoster(cast.profilePath)
            itemView.findViewById<TextView>(R.id.tvCastName).text = cast.name
            itemView.findViewById<TextView>(R.id.tvCharacterName).text = cast.character
            //TODO:add more fields from cast
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cast_card, parent, false)
        return CastViewHolder(itemView)
    }

    override fun getItemCount() = casts.size

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(casts[position])
    }
}